# 🔧 Guia de Correção: Grafana com Reverse Proxy HTTPS

## ✅ Mudanças Realizadas

### 1. **Workflow do GitHub (ci_cd.yml)** - ATUALIZADO
Ajustadas as variáveis de ambiente do Grafana:

```yaml
-e "GF_SERVER_ROOT_URL=/grafana/"              # URL relativa
-e "GF_SERVER_SERVE_FROM_SUB_PATH=true"        # Ativa suporte a subpath
-e "GF_SERVER_DOMAIN=miguelprogrammer-challenge.duckdns.org"
```

### 2. **Arquivo Nginx** - NOVO (nginx-grafana.conf)
Adicionados headers críticos que faltavam:

```nginx
proxy_set_header X-Script-Name /grafana;
proxy_cookie_path / /grafana/;
proxy_redirect / /grafana/;
```

---

## 🚀 Passos para Aplicar no EC2

### **PASSO 1: Backup da configuração atual**
```bash
sudo cp /etc/nginx/conf.d/app.conf /etc/nginx/conf.d/app.conf.backup
```

### **PASSO 2: Atualizar o arquivo Nginx**
Abra o arquivo:
```bash
sudo nano /etc/nginx/conf.d/app.conf
```

E substitua o bloco de GRAFANA por:

```nginx
    # 2. GRAFANA (CORRIGIDO PARA SUBPATH)
    location /grafana/ {
        proxy_pass http://localhost:3000/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Script-Name /grafana;
        proxy_cookie_path / /grafana/;

        # Ajustes de redirecionamento para evitar tela branca
        proxy_buffering off;
        proxy_redirect http:// https://;
        proxy_redirect / /grafana/;
    }
```

**Salvar**: Ctrl+O → Enter → Ctrl+X

### **PASSO 3: Validar configuração do Nginx**
```bash
sudo nginx -t
```

Esperado:
```
nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
nginx: configuration will be successful
```

### **PASSO 4: Recarregar Nginx**
```bash
sudo systemctl reload nginx
```

### **PASSO 5: Fazer deploy do Grafana via GitHub**
Faça um commit e push na branch `developer`:
```bash
git add .github/workflows/ci_cd.yml
git commit -m "fix: grafana configuration for https subpath"
git push origin developer
```

O pipeline vai redeploy do Grafana com as variáveis corretas.

### **PASSO 6: Verificar se está funcionando**
```bash
# Aguarde 2-3 minutos e acesse:
https://miguelprogrammer-challenge.duckdns.org/grafana/
```

**Login**: `admin / admin123`

---

## 🔍 Diagnóstico em Caso de Erro

```bash
# 1. Verificar se Grafana está rodando
docker ps | grep grafana

# 2. Verificar logs do Grafana
docker logs grafana | tail -50

# 3. Verificar conectividade interna
docker exec grafana curl -I http://localhost:3000/

# 4. Verificar logs do Nginx
sudo tail -50 /var/log/nginx/error.log

# 5. Testar acesso de dentro do container
docker exec -it grafana bash
curl -v http://localhost:3000/api/health
exit
```

---

## 📝 Resumo das Variáveis Grafana

| Variável | Valor | Razão |
|----------|-------|-------|
| `GF_SERVER_ROOT_URL` | `/grafana/` | URL relativa ao proxy reverso |
| `GF_SERVER_SERVE_FROM_SUB_PATH` | `true` | Ativa suporte a subpaths |
| `GF_SERVER_DOMAIN` | `miguelprogrammer-challenge.duckdns.org` | Seu domínio |

---

## ⚠️ Headers Nginx Críticos

```nginx
proxy_set_header X-Script-Name /grafana;      # Informa o subpath ao Grafana
proxy_cookie_path / /grafana/;                 # Ajusta cookies para o subpath
proxy_redirect / /grafana/;                    # Redireciona respostas para o subpath
```

Sem esses headers, o Grafana não carrega os arquivos estáticos corretamente!

