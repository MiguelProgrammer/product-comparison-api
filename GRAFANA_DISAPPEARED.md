# 🚨 Solução Rápida - Grafana Desapareceu

## 🔴 Problema
Grafana não está mais acessível em `https://miguelprogrammer-challenge.duckdns.org/grafana/`

---

## ✅ Solução Imediata (Opção 1 - Recomendada)

### Execute no seu EC2:

```bash
# 1. Copiar o script
curl -o fix-grafana-complete.sh https://raw.githubusercontent.com/seu-repo/fix-grafana-complete.sh

# 2. Dar permissão
chmod +x fix-grafana-complete.sh

# 3. Executar
./fix-grafana-complete.sh
```

**Ou manualmente:**

```bash
# Parar containers
docker stop grafana prometheus || true

# Remover containers
docker rm grafana prometheus || true

# Limpar volume
docker volume rm grafana-storage || true

# Recrear Grafana
docker volume create grafana-storage

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
  -e "GF_SERVER_SERVE_FROM_SUB_PATH=true" \
  -e "GF_SERVER_ROOT_URL=https://miguelprogrammer-challenge.duckdns.org/grafana/" \
  grafana/grafana:latest

# Aguardar 10 segundos
sleep 10

# Verificar
docker ps | grep grafana
```

---

## 🔍 Diagnóstico

```bash
# Ver status dos containers
docker ps -a

# Ver logs
docker logs grafana | tail -30

# Testar conectividade
docker exec grafana curl http://localhost:3000/api/health

# Verificar Nginx
sudo nginx -t
sudo systemctl status nginx
```

---

## 🚀 Solução 2 - Redeploy via Pipeline

1. Faça commit e push na branch `developer`:
```bash
git add .github/workflows/ci_cd.yml
git commit -m "fix: simplify grafana configuration"
git push origin developer
```

2. Aguarde o pipeline terminar (~5-10 minutos)
3. Acesse `https://miguelprogrammer-challenge.duckdns.org/grafana/`

---

## 📝 Mudanças Realizadas no Workflow

✅ **Removidas variáveis que causavam conflito:**
- ❌ `GF_SERVER_DOMAIN` (não necessária)
- ❌ `GF_SECURITY_COOKIE_SAMESITE` (não necessária)
- ❌ `GF_SECURITY_COOKIE_SECURE` (não necessária)
- ❌ `GF_SECURITY_ALLOW_EMBED_FRAMES` (não necessária)
- ❌ `GF_SERVER_PROTOCOL=http` (conflito com HTTPS ROOT_URL)

✅ **Mantidas apenas:**
- ✓ `GF_SECURITY_ADMIN_PASSWORD=admin123`
- ✓ `GF_SECURITY_ADMIN_USER=admin`
- ✓ `GF_USERS_ALLOW_SIGN_UP=false`
- ✓ `GF_SERVER_SERVE_FROM_SUB_PATH=true`
- ✓ `GF_SERVER_ROOT_URL=https://miguelprogrammer-challenge.duckdns.org/grafana/`

---

## ✅ Checklist Pós-Correção

- [ ] Containers inicializados com sucesso
- [ ] `docker ps` mostra grafana rodando
- [ ] Acesso a `https://miguelprogrammer-challenge.duckdns.org/grafana/` funciona
- [ ] Login com `admin / admin123` funciona
- [ ] Dashboard carrega sem erros

---

**Próximo passo:** Execute um dos scripts acima e me confirme se funcionou! 🎯

