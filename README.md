# Chaotical

Chaotical is a terminal-based application designed for command management, running minecraft simulations in the terminal and more. It includes built-in functionalities for managing commands, starting simulations and more. Below is a list and explanation to the available commands and features of Chaotical.

## Features

- **Command Management:** Add commands that can store data you need all the time and remove them whenever you like.
- **Simulation Control:** Simulate Minecraft Endfights (Onecycle or Oneshot) from your terminal to get results on how fast a player could potentially beat the dragon on the specified seed.
- **Ciphered Text:** Encrypts outputs using Caesar Cipher to ensure no one can sneak peek into your work on the terminal.
- **Regular Movement:** To ensure you are active when working on the terminal, the terminal moves to a random point in a radius of 100 pixels from its current position every now and then.
- **Ad Pop-ups:** Ads in the terminal to keep you entertained while being productive.

## Commands

### General Commands

- **`add <name:response>`**  
  Adds a new command to `commands.txt`. The format should be `name:response` (no spaces in name).

- **`remove <name>`**  
  Removes an existing command from `commands.txt`.

- **`clear`**  
  Clears the terminal.

- **`search <query>`**  
  Opens your default web browser to search Google for the specified query.

- **`uncipher`**  
  Deciphers the last ciphered output using Caesar Cipher.

### Simulation Commands

- **`startsimulation <seeds>`**  
  Starts a simulation with the given comma-separated seeds (results are not printed as output in the terminal rather they are printed to the console).

- **`setmode <mode>`**  
  Sets the simulation mode. Valid modes are `onecycle` and `oneshot`.

- **`setnum <number>`**  
  Sets the number of simulations you want to run. Must be greater than 0. Default is 1000.

## Built-In Endfight Simulators

Chaotical includes built-in functionality for endfight simulations (Stay productive).

## Encryption and Decryption

Chaotical uses Caesar Cipher for encrypting and decrypting outputs.

## Getting Started

1. Clone the Chaotical repository or download the latest release.
2. If you cloned the repository run main.java but if you downloaded the latest release then open the `.jar` file using java (JRE/JDK 21 is recommended).

## Support

For issues or further assistance, please open an issue on the repository page or contact the project maintainer (Discord: crystal_thepvpgod).