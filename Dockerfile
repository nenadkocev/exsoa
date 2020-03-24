FROM postgres
ENV POSTGRES_PASSWORD admin
ENV POSTGRES_USER postgres
COPY init.sql /docker-entrypoint-initdb.d/