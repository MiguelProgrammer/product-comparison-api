#!/bin/bash

echo "=== VERIFICAÇÃO COMPLETA DO AMBIENTE ==="
echo ""

echo "1. Verificando containers..."
docker-compose ps
echo ""

echo "2. Testando conectividade Prometheus..."
curl -s http://localhost:9090/api/v1/query?query=up | jq '.' 2>/dev/null || echo "Prometheus não acessível ou jq não instalado"
echo ""

echo "3. Testando API Health..."
curl -s http://localhost:8080/actuator/health | jq '.' 2>/dev/null || echo "API não acessível"
echo ""

echo "4. Verificando métricas da API..."
echo "Métricas business encontradas:"
curl -s http://localhost:8080/actuator/prometheus | grep -c "business_" || echo "Nenhuma métrica business encontrada"
echo ""

echo "5. Testando Grafana..."
curl -s -o /dev/null -w "%{http_code}" http://localhost:3000
echo " <- Status HTTP do Grafana"
echo ""

echo "6. Verificando targets do Prometheus..."
curl -s http://localhost:9090/api/v1/targets | jq '.data.activeTargets[] | {job: .labels.job, health: .health}' 2>/dev/null || echo "Não foi possível verificar targets"
echo ""

echo "7. Logs recentes dos containers:"
echo "--- Prometheus ---"
docker logs prometheus --tail 5 2>/dev/null || echo "Erro ao acessar logs do Prometheus"
echo ""
echo "--- Grafana ---"
docker logs grafana --tail 5 2>/dev/null || echo "Erro ao acessar logs do Grafana"
echo ""
echo "--- API ---"
docker logs product-comparison-api --tail 5 2>/dev/null || echo "Erro ao acessar logs da API"
echo ""

echo "=== COMANDOS PARA GERAR DADOS DE TESTE ==="
echo ""
echo "# Criar produto:"
echo 'curl -X POST http://localhost:8080/api/v1/product -H "Content-Type: application/json" -d '"'"'{"name":"Teste","price":100,"description":"Teste","rating":"FIVE_STARS","specification":"Teste","url":"http://test.com"}'"'"''
echo ""
echo "# Fazer comparação:"
echo 'curl -X POST http://localhost:8080/api/v1/compare/products -H "Content-Type: application/json" -d '"'"'{"productIds":[1]}'"'"''
echo ""

echo "=== ACESSOS ==="
echo "Grafana: http://localhost:3000 (admin/admin123)"
echo "Prometheus: http://localhost:9090"
echo "API: http://localhost:8080"
echo "Swagger: http://localhost:8080/swagger-ui.html"