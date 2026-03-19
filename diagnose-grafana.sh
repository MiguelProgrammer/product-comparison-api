#!/bin/bash

# ====================================================================
# Script de Diagnóstico - Grafana Desapareceu
# Execute no EC2!
# ====================================================================

echo "🔍 DIAGNÓSTICO DO GRAFANA"
echo "================================"
echo ""

# 1. Verificar se container está rodando
echo "1️⃣  Verificando status dos containers..."
echo ""
docker ps -a | grep -E "grafana|prometheus|product-comparison-app"
echo ""

# 2. Verificar logs do Grafana
echo "2️⃣  Últimos 30 linhas de log do Grafana..."
echo ""
docker logs grafana 2>&1 | tail -30
echo ""

# 3. Verificar se Nginx está rodando
echo "3️⃣  Status do Nginx..."
sudo systemctl status nginx
echo ""

# 4. Verificar logs do Nginx
echo "4️⃣  Erros no Nginx..."
sudo tail -20 /var/log/nginx/error.log
echo ""

# 5. Testar conectividade local
echo "5️⃣  Testando conectividade local..."
docker exec grafana curl -s http://localhost:3000/api/health | head -10
echo ""

# 6. Verificar configuração do Grafana
echo "6️⃣  Variáveis de ambiente do Grafana..."
docker inspect grafana | grep -A 50 "\"Env\""
echo ""

echo "================================"
echo "✅ Diagnóstico Completo"

