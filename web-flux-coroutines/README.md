# Reative webflux

### Docker database
```shell
docker run  --name local-webflux-postgres -p 5432:5432 -e POSTGRES_USER=webflux -e POSTGRES_PASSWORD=webflux -d postgres
```