import csv
import socket
import sys
import time

# Function to start the TCP server
def start_tcp_server(file_name, host='127.0.0.1', port=8899, chunk_size=1024):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((host, port))
    server_socket.listen(1)  # Listen for incoming connections (1 connection in the queue)

    print(f"Server listening on {host}:{port}")

    while True:
        conn, addr = server_socket.accept()
        print(f"Connected by {addr}")

        with open(file_name, newline='') as file:
            csv_reader = csv.reader(file)
            while True:
                chunk = ''.join(','.join(row) + '\n' for row in csv_reader)
                print(chunk)
                if not chunk:
                    break

                conn.send(chunk.encode())
                time.sleep(0.1)

        conn.close()
        print("Data sent. Connection closed.")

    server_socket.close()

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script_name.py file_name.csv")
    else:
        file_name = sys.argv[1]  # File name with CSV data
        start_tcp_server(file_name)
