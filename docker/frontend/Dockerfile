FROM hseeberger/scala-sbt:11.0.11_1.5.4_2.13.6

RUN apt-get update && apt-get install -y \
  nodejs

WORKDIR /app

CMD ["sbt", "project frontend", "~fastLinkJS"]
