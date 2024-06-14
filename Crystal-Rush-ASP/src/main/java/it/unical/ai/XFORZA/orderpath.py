import re
from collections import defaultdict

def read_input_file(file_path):
    with open(file_path, 'r') as file:
        content = file.read()
    return content

def write_output_file(file_path, content):
    with open(file_path, 'w') as file:
        file.write(content)

def group_connections(connections):
    grouped = defaultdict(list)
    for conn in connections:
        match = re.match(r'connesso\((\d+),\d+\)', conn)
        if match:
            prefix = match.group(1)[:2]  # Prende le prime due cifre del primo numero
            grouped[prefix].append(conn)
    return grouped

def format_connections(grouped_connections):
    formatted_output = ""
    for key in sorted(grouped_connections):
        formatted_output += "\n".join(grouped_connections[key]) + "\n\n"
    return formatted_output.strip()

def main():
    input_path = 'input.txt'
    output_path = 'input.txt'
    
    content = read_input_file(input_path)
    
    # Trova tutte le connessioni utilizzando una regex
    connections = re.findall(r'connesso\(\d+,\d+\)', content)
    
    # Raggruppa le connessioni
    grouped_connections = group_connections(connections)
    
    # Formatta le connessioni
    formatted_content = format_connections(grouped_connections)
    
    # Scrivi il nuovo contenuto nel file
    write_output_file(output_path, formatted_content)

if __name__ == "__main__":
    main()
