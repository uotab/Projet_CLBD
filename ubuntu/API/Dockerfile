FROM tensorflow/tensorflow

EXPOSE 5000

COPY requirements.txt .

RUN pip install --upgrade pip

RUN pip install -r requirements.txt

RUN apt-get -y update && apt-get install -y libsndfile1

RUN apt-get install -y ffmpeg

COPY . .

CMD ["flask", "run","--host=0.0.0.0","--port=5000" ]
