Test your Othello program
--------------------------
This folder contains code for testing your Othello program. 

On a linux machine, do the following:
- Make sure that the scripts 'othellostart' and 'othello' are executable. If not, write `chmod +x othellostart othello`.

- Run the 'othellostart' script with three parameters, indicating which programs should play against each other and a time limit:
  - `./othellostart` *`white_player`* *`black_player`* *`time_limit`*
  
  - For instance, if your home directory is '/home/abc123/' and you have placed your 'othello' script in ~/edu/5DV122/lab1/, then you can play the test program against your own (as black) with a 5s time limit by writing `./othellostart ./othello /home/abc123/edu/5DV122/lab1/othello 5`

  - If you would like to play against a friend, just replace ./othello with the correct path to your friend's script
