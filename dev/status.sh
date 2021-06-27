tmux new-session -s Soogle -n Elasticsearch 'docker logs -f elasticsearch' \; \
  select-window -t Elasticsearch
