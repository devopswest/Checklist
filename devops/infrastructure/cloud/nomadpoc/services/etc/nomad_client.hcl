# Increase log verbosity
log_level = "DEBUG"


# Setup data dir
bind_addr = "0.0.0.0"
data_dir = "/var/lib/nomad"
region ="us-east"

advertise {
  # We need to specify our host's IP because we can't
  # advertise 0.0.0.0 to other nodes in our cluster.
  rpc = "VAR_ADV_IP_ADDRESS:4647"
}

# Enable the client
client {
    enabled = true
    network_interface="enp0s8"
    # For demo assume we are talking to server1. For production,
    # this should be like "nomad.service.consul:4647" and a system
    # like Consul used for service discovery.
    servers = ["VAR_JOIN_IP_ADDRESSES:4647"]
}

# Modify our port to avoid a collision with server1
#ports {
#    http = 5656
#}
