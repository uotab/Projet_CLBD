from flask import Flask, render_template , request, jsonify, send_file
from werkzeug.utils import secure_filename
import os
import tensorflow as tf
import magic
import numpy as np
import librosa
from pydub import AudioSegment
import io
import pandas as pd
import matplotlib.pyplot as plt
from math import pi, ceil
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler, MinMaxScaler
import statistics
import random

MODEL = tf.keras.models.load_model('MFCCAudioAugmentationV2')

genres_musicaux = ['blues','classical','country','disco','hiphop','jazz','metal','pop','reggae','rock']

# Load the song data into a DataFrame
songs_df = pd.read_csv('songinfos.csv')
columns_to_cluster = ['zero_cross_sum','zero_cross_rate_mean','chromagram_mean','mfcc_mean_global', 
          'mfcc1_mean','mfcc2_mean','mfcc3_mean','mfcc4_mean','mfcc5_mean','mfcc6_mean',
          'mfcc7_mean','mfcc8_mean','mfcc9_mean','mfcc10_mean','mfcc11_mean','mfcc12_mean','mfcc13_mean',
          'mfcc14_mean','mfcc15_mean','mfcc16_mean','mfcc17_mean','mfcc18_mean','mfcc19_mean','mfcc20_mean']
mms = MinMaxScaler()
songs_scaled = mms.fit_transform(songs_df[columns_to_cluster])

columns_to_cluster_scaled = ['zero_cross_sum','zero_cross_rate_mean','chromagram_mean','mfcc_mean_global', 
          'mfcc1_mean','mfcc2_mean','mfcc3_mean','mfcc4_mean','mfcc5_mean','mfcc6_mean',
          'mfcc7_mean','mfcc8_mean','mfcc9_mean','mfcc10_mean','mfcc11_mean','mfcc12_mean','mfcc13_mean',
          'mfcc14_mean','mfcc15_mean','mfcc16_mean','mfcc17_mean','mfcc18_mean','mfcc19_mean','mfcc20_mean']
df_songs_scaled = pd.DataFrame(songs_scaled, columns=columns_to_cluster_scaled)

k=6

model = KMeans(n_clusters=k, random_state=42).fit(songs_scaled)
pred = model.predict(songs_scaled)
df_songs_scaled['cluster'] = model.labels_



app = Flask(__name__)
@app.route("/",methods=["GET"])
def helloo():
    return "<p>Hello</p>"


@app.route('/audio',methods=["POST"])
def audio():
    title = request.args.get('title')
    filename = title + '.mp3'
    return send_file('audio8sec/'+filename, mimetype='audio/mp3')

@app.route("/upload", methods=["POST"])
def upload():
    audio_data = request.data


#   audio = AudioSegment.from_file(audio_bytes, format="mp4")
    #Convert it to a wav format
#    audio = audio.set_channels(1)
#    audio = audio.set_sample_width(2)
#    audio = audio.set_frame_rate(44100)
#    audio = audio.export("audio.wav", format="wav")
    if not audio_data:
      return jsonify({"error": "No audio data in the request"}), 400
    
    file_type = magic.from_buffer(audio_data, mime=True)
    if file_type != "video/mp4":
      print(file_type)
      return jsonify({"error": "File not a mpeg file"}), 400
    
    with open("audio.mp4","wb") as f:
        f.write(audio_data)
    
    song3,sr=librosa.load("audio.mp4", duration=3)
    print("oui")
    data = np.array([librosa.feature.mfcc(y=song3,sr=sr, n_fft=1012, hop_length=256, n_mfcc=20)])
    data = tf.expand_dims(data, axis=-1)
    prediction = MODEL.predict(data)
    class_label = np.argmax(prediction)

    song8,sr = librosa.load("audio.mp4", duration=8)
    zero_cross_sum = sum(librosa.zero_crossings(song8, pad=False))
    zero_cross_rate_mean = statistics.mean(librosa.feature.zero_crossing_rate(song8)[0])
    chromagram = librosa.feature.chroma_stft(y=song8, sr=sr, hop_length=5000)
    mean = []
    for i in range(len(chromagram)):
      mean.append(statistics.mean(chromagram[i]))
    chromagram_mean = statistics.mean(mean)
    mfcc = librosa.feature.mfcc(y=song8, sr=sr, n_fft=1012, hop_length=256, n_mfcc=20)
    mfcc_means = []
    for i in range(len(mfcc)):
      mfcc_means.append(statistics.mean(mfcc[i]))
    mfcc_mean_global = statistics.mean(mfcc_means)
    data = [zero_cross_sum,zero_cross_rate_mean,chromagram_mean,mfcc_mean_global]
    for i in range(len(mfcc_means)):
      data.append(mfcc_means[i])
    
    cluster = model.predict([data])
    df_songs_joined = pd.concat([songs_df,df_songs_scaled], axis=1).set_index('cluster')
    cluster

    list_of_cluster=df_songs_joined.loc[cluster[0], ['name']].iloc[:,0].tolist()
    reco = random.sample(list_of_cluster,2)
    print(*list_of_cluster, sep=', ')

    response = {"genre":genres_musicaux[class_label], "reco1":reco[0], "reco2":reco[1]}
    return jsonify(response)


if __name__ == "__main__":
  app.run()
