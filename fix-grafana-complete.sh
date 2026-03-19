#!/bin/bash

# ====================================================================
# Script de Correção Total - Grafana Desaparecido
# Execute no EC2!
# chmod +x fix-grafana-complete.sh
# ./fix-grafana-complete.sh
# ====================================================================

set -e

echo "🔧 INICIANDO CORREÇÃO COMPLETA DO GRAFANA"
echo "=========================================="
echo ""

# 1. Parar containers antigos
echo "⏹️  Parando containers antigos..."
docker stop grafana || true
docker stop prometheus || true
docker stop product-comparison-app || true
sleep 2

# 2. Remover containers
echo "🗑️  Removendo containers..."
docker rm grafana || true
docker rm prometheus || true
docker rm product-comparison-app || true

# 3. Limpar volumes se necessário
echo "🧹 Limpando volumes de cache..."
docker volume rm grafana-storage || true
echo "✅ Volumes limpos"

# 4. Criar rede
echo "🌐 Criando rede Docker..."
docker network create monitoring-network || true

# 5. Pull de imagens
echo "📦 Fazendo pull das imagens..."
docker pull grafana/grafana:latest
docker pull prom/prometheus
echo "✅ Imagens prontas"

# 6. Criar volumes
echo "💾 Criando volumes..."
docker volume create grafana-storage

# 7. Configurar Prometheus
echo "⚙️  Configurando Prometheus..."
mkdir -p ~/prometheus
cat > ~/prometheus/prometheus.yml <<'EOF'
global:
  scrape_interval: 15s
scrape_configs:
  - job_name: 'spring-boot-app'
    metrics_path: '/actuator/prometheus'
    scheme: https
    tls_config:
      insecure_skip_verify: true
    static_configs:
      - targets: ['miguelprogrammer-challenge.duckdns.org']
EOF
echo "✅ Prometheus configurado"

# 8. Subir Prometheus
echo "🚀 Iniciando Prometheus..."
docker run -d \
  --name prometheus \
  --network monitoring-network \
  --network-alias prometheus \
  -p 9090:9090 \
  -v ~/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
  prom/prometheus
echo "✅ Prometheus iniciado"

# 9. Subir Grafana (SIMPLIFICADO)
echo "🚀 Iniciando Grafana..."
docker run -d \
  --name grafana \
  --user root \
  --network monitoring-network \
  --network-alias grafana \
  -p 3000:3000 \
  -v grafana-storage:/var/lib/grafana \
  -e "GF_SECURITY_ADMIN_PASSWORD=admin123" \
  -e "GF_SECURITY_ADMIN_USER=admin" \
  -e "GF_USERS_ALLOW_SIGN_UP=false" \
  -e "GF_INSTALL_PLUGINS=grafana-piechart-panel" \
  -e "GF_SERVER_SERVE_FROM_SUB_PATH=true" \
  -e "GF_SERVER_ROOT_URL=https://miguelprogrammer-challenge.duckdns.org/grafana/" \
  grafana/grafana:latest
echo "✅ Grafana iniciado"

# 10. Aguardar inicialização
echo "⏳ Aguardando inicialização (15s)..."
sleep 15

# 11. Verificar status
echo ""
echo "✅ CONTAINERS EM EXECUÇÃO:"
docker ps | grep -E "grafana|prometheus|product"

echo ""
echo "📋 PRÓXIMOS PASSOS:"
echo "   1. Aguarde o App Java ser redeplayed pelo pipeline"
echo "   2. Acesse: https://miguelprogrammer-challenge.duckdns.org/grafana/"
echo "   3. Login: admin / admin123"
echo ""
echo "🔍 DIAGNÓSTICO:"
echo "   docker logs grafana | tail -20"
echo "   docker logs prometheus | tail -20"
echo ""
echo "✅ Correção Completa!"

