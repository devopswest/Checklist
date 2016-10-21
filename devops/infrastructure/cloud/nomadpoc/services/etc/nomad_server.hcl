# Increase log verbosity
log_level = "DEBUG"

bind_addr = "0.0.0.0"
data_dir = "/var/lib/nomad"
region ="us-east"

advertise {
  # We need to specify our host's IP because we can't
  # advertise 0.0.0.0 to other nodes in our cluster.
  rpc = "VAR_ADV_IP_ADDRESS:4647"
}


# Enable the server
server {
    enabled = true

    # Self-elect, should be 3 or 5 for production
    bootstrap_expect = 1
}
