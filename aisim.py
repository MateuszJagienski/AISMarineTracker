import socket
import time

# Function to read data from a text file
def read_data_from_file(file_name):
    with open(file_name, 'r') as file:
        data = file.readlines()
    return data

# Function to send data via UDP
def send_data_via_udp(data, host='127.0.0.1', port=12345):
    udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    num = 0
    for line in data:
        num += 1
        if ',2,' in line:  # Check if the line contains ',2,'
            udp_socket.sendto(line.encode(), (host, port))
            print(line)
            num = 0  # Reset the counter for immediate sending
        elif num == 3:  # If no ',2,' found and count reaches 3, delay sending
            time.sleep(1)
            num = 0
        else:
            print(line)
            udp_socket.sendto(line.encode(), (host, port))

    udp_socket.close()

if __name__ == "__main__":
    file_name = 'src/main/resources/META-INF/resources/aisdata/allAisData.txt'  # File name with data
    data = read_data_from_file(file_name)

    # UDP Host and Port
    udp_host = '127.0.0.1'  # Change to the address you want to connect to
    udp_port = 12346  # Change to the desired port

    send_data_via_udp(data, udp_host, udp_port)