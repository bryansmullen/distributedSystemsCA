openssl req -x509 -newkey rsa:4096 -keyout ./my-private-key.pem -out ./my-public-key-cert.pem -days 365 -nodes -subj '/CN=localhost'
