import socket
import time
import sys

# Function to read data from a text file
def read_data_from_file(file_name):
    with open(file_name, 'r') as file:
        data = file.readlines()
    return data

# Function to send data via UDP
def send_data_via_udp(data, host='192.168.1.12', port=4712):
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
    if len(sys.argv) < 2 or len(sys.argv) > 4:
        print("Usage: python script_name.py file_name [udp_host] [udp_port]")
    else:
        file_name = sys.argv[1]  # File name with data
        data = read_data_from_file(file_name)

        udp_host = '192.168.1.12'
        udp_port = 4712

        print("Usage: python script_name.py file_name [udp_host] [udp_port]")
        if len(sys.argv) >= 3:
            udp_host = sys.argv[2]  # UDP Host

        if len(sys.argv) == 4:
            udp_port = int(sys.argv[3])  # UDP Port

        send_data_via_udp(data, udp_host, udp_port)
