ARG ELASTIC_VERSION

# https://www.docker.elastic.co/
FROM docker.elastic.co/beats/metricbeat:${ELASTIC_VERSION}
COPY --chown=root:metricbeat ./config/metricbeat.yml /usr/share/metricbeat/metricbeat.yml