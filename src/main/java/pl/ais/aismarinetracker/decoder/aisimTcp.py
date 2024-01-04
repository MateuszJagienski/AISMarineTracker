import socket
import sys
import time

# Function to read data from a text file
def read_data_from_file(file_name):
    with open(file_name, 'r') as file:
        data = file.readlines()
    return data

# Function to start the TCP server
def start_tcp_server(file_name, host='127.0.0.1', port=8899):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((host, port))
    server_socket.listen(1)  # Listen for incoming connections (1 connection in the queue)

    print(f"Server listening on {host}:{port}")

    while True:
        conn, addr = server_socket.accept()
        print(f"Connected by {addr}")

        data = read_data_from_file(file_name)

        for line in data:
            print(line)
            conn.send(line.encode())
            time.sleep(0.1)


        conn.close()
        print("Data sent. Connection closed.")

    server_socket.close()

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script_name.py file_name")
    else:
        file_name = sys.argv[1]  # File name with data
        start_tcp_server(file_name)
