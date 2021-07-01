tmux new-session -s Soogle -n Elasticsearch 'docker logs -f elasticsearch' \; \
  neww -n Frontend 'docker logs -f scalajs' \; \
  select-window -t Elasticsearch
