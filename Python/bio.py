import math
import numpy as np
import face_recognition
import os
import sys
from socket import *

""" PARAMETRI INIZIALI """
zm_fc = 0.50
TCP_IP = "143.225.85.144"  # Indirizzo IP del server 
T_PORT = 65535  # Porta del server

socket_tcp = socket(AF_INET, SOCK_STREAM)
socket_tcp.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
socket_tcp.bind((TCP_IP, T_PORT))
socket_tcp.listen(1)

def receiveImage(conn):
    try:
        with open('/Users/Bia/Desktop/ReceivedImage.png', 'wb') as img:
            # Ricevi la dimensione dell'array di byte all'inizio del flusso
            size_bytes = conn.recv(4)
            image_size = int.from_bytes(size_bytes, byteorder='big')

            # Ricevi l'immagine
            remaining_size = image_size
            while remaining_size > 0:
                data = conn.recv(min(1024, remaining_size))
                if not data:
                    # Se non ci sono più dati da ricevere, esci dal ciclo
                    break
                img.write(data)
                remaining_size -= len(data)

        print("Immagine ricevuta dal dispositivo Android")

    except Exception as e:
        print("Errore durante la ricezione dell'immagine:", str(e))

def sendKey(key, conn):
    try:
        if key is not None:
            conn.send(key.encode())
            print("Chiave biometrica inviata al dispositivo Android")
        else:
            # Se il volto non è stato rilevato, invia un messaggio di "NO_FACE_DETECTED"
            conn.send("NO_FACE_DETECTED".encode())
            print("Nessun volto rilevato. Invio del messaggio di 'NO_FACE_DETECTED'")
    except Exception as e:
        print("Errore durante l'invio della chiave biometrica:", str(e))
    finally:
        conn.close()

def get_frame():
    try:
        frame = face_recognition.load_image_file('/Users/Bia/Desktop/ReceivedImage.png')
        face_locations = face_recognition.face_locations(frame)

        if len(face_locations) > 0:
            print("Volto rilevato.")
            face_encodings = face_recognition.face_encodings(frame, face_locations)
            return True, face_encodings[0]
        else:
            print("Nessun volto rilevato.")
            return False, None

    except Exception as e:
        print("Errore in get_frame:", str(e))
        return False, None

def start():
    try:
        ret, face_encoding = get_frame()
        if ret and face_encoding is not None:
            print("Chiave biometrica generata:", str(face_encoding))
            return str(face_encoding)
        else:
            print("Nessun volto rilevato.")
            return None
    except Exception as e:
        print("Errore nella generazione della chiave biometrica:", str(e))
    return None

if __name__ == '__main__':
    print("In attesa del dispositivo Android ...")

    while True:
        conn, addr = socket_tcp.accept()

        print("Connessione con il dispositivo Android stabilita. Pronto per ricevere l'immagine")
        receiveImage(conn)
        print("Generazione della chiave biometrica in corso ...")

        chiave_biometrica = start()
        if chiave_biometrica is not None:
            print("Invio della chiave al dispositivo Android ...")
            sendKey(chiave_biometrica, conn)
            conn.close()  # Chiudo la connessione dopo aver inviato la chiave
            
        else:
            print("Nessun volto rilevato. Si prega di reinserire le informazioni dell'utente.")
            chiave_messaggio = "NO_FACE_DETECTED"

            # Chiudo la connessione prima di inviare il messaggio
            conn.close()

            # Creo una nuova connessione per inviare il messaggio
            conn, addr = socket_tcp.accept()
            conn.send(chiave_messaggio.encode())

            conn.close()
