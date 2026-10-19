
FROM mysql:8.4

WORKDIR /tmp

# Copy database dump files
COPY test_db/*.dump /tmp/
COPY test_db/*.dump /docker-entrypoint-initdb.d/

# Run the database initialization script
COPY test_db/employees.sql /docker-entrypoint-initdb.d/employees.sql

ENV MYSQL_ROOT_PASSWORD=example
