#!/bin/bash
# Script for running two othello programs agianst eachother
# Usage: othellostart white_player black_player time_limit
# The time limit is in seconds
# Author: Henrik Björklund
# Edited by Ola Ringdahl

if [ $# -lt 3 ]; then
  echo "Too few arguments."
	echo "Usage: othellostart white_player black_player time_limit"
	exit 1
fi

pos="WEEEEEEEEEEEEEEEEEEEEEEEEEEEOXEEEEEEXOEEEEEEEEEEEEEEEEEEEEEEEEEEE"
java Printer $pos

white="$1"
black="$2"
time="$3"
endgame="no"
whitepass="no"
blackpass="no"
tomove="black"
nMoves=0
whiteTmax=0
blackTmax=0
whiteTtot=0
blackTtot=0
while [ "$endgame" != "yes" ]
do
    echo ""
    echo ""
    echo "White to move"
    echo $pos
    STARTTIME=$(date +%s)
    move=`$white $pos $time`
    ENDTIME=$(date +%s)
    echo $move    
    pos=`java Mover "$pos" "$move"`
    if [ "$move" == "pass" ]
    then
        whitepass="yes"
        if [ "$blackpass" == "yes" ]
        then
            endgame="yes"
        fi
    else
        whitepass="no"
    fi
    java Printer $pos
    whiteT=$(($ENDTIME - $STARTTIME))
    echo "It took $whiteT seconds to make this move..."
    whiteTtot=$(($whiteTtot + $whiteT))
    if [ "$whiteT" -gt "$whiteTmax" ] 
    then
        whiteTmax=$whiteT
    fi
    
    if [ "$endgame" != "yes" ]
    then
        echo ""
        echo ""
        echo "Black to move"
        echo $pos
		STARTTIME=$(date +%s)
        move=`$black $pos $time`
        ENDTIME=$(date +%s)
        echo $move
        pos=`java Mover "$pos" "$move"`
        if [ "$move" == "pass" ]
        then
            blackpass="yes"
            if [ "$whitepass" == "yes" ]
            then
                endgame="yes"
            fi
        else
            blackpass="no"
        fi
        java Printer $pos
        blackT=$(($ENDTIME - $STARTTIME))
        echo "It took $blackT seconds to make this move..."
        blackTtot=$(($blackTtot + $blackT))
		if [ "$blackT" -gt "$blackTmax" ]
		then
	    	blackTmax=$blackT
		fi
    fi
    
    nMoves=$(($nMoves+1))
done

whitecount=`java Counter "$pos"`
if [ $whitecount -gt 0 ] 
then
  winner="White"  
else
  winner="Black"
  whitecount=$((-1 * $whitecount))
fi

whiteTmean=$(awk "BEGIN {printf \"%.1f\", ${whiteTtot} / ${nMoves}}")
blackTmean=$(awk "BEGIN {printf \"%.1f\", ${blackTtot} / ${nMoves}}")

echo "***************************************"
echo "Results for $white vs. $black:"
echo "$winner won with $whitecount points"
echo "Average time for white: $whiteTmean s (max: $whiteTmax s)"
echo "Average time for black: $blackTmean s (max: $blackTmax s)" 
echo "***************************************"

