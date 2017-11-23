#!/bin/bash
# Script for running two othello programs agianst eachother
# Usage: othellostart white_player black_player time_limit
# The time limit is in seconds
# Author: Henrik Björklund
# Edited by Ola Ringdahl

if [ $# -lt 4 ]; then
  echo "Too few arguments."
	echo "Usage: othellostart white_player black_player time_limit match_number"
	exit 1
fi

white_wins=0
black_wins=0
white_total_score=0
black_total_score=0

for (( game_number=0; game_number<$4; game_number++ ))
do


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
      white_wins=$((white_wins+1))
      white_total_score=$((white_total_score + whitecount))
    else
      winner="Black"
      black_wins=$((black_wins+1))
      whitecount=$((-1 * $whitecount))
      black_total_score=$((black_total_score + whitecount))
    fi

    whiteTmean=$(awk "BEGIN {printf \"%.1f\", ${whiteTtot} / ${nMoves}}")
    blackTmean=$(awk "BEGIN {printf \"%.1f\", ${blackTtot} / ${nMoves}}")

    echo "***************************************"
    echo "Results for $white vs. $black:"
    echo "$winner won with $whitecount points"
    echo "Average time for white: $whiteTmean s (max: $whiteTmax s)"
    echo "Average time for black: $blackTmean s (max: $blackTmax s)"
    echo "***************************************"
done

if [ "$white_wins" == 0 ]
then
    white_mean=0
else
    white_mean=$((white_total_score/white_wins))
fi

if [ "$black_wins" == 0 ]
then
    black_mean=0
else
    black_mean=$((black_total_score/black_wins))
fi

echo "***************************************"
echo "Out of $game_number games: "
echo "white ($white) won $white_wins times with an average of $white_mean"
echo "black ($black) won $black_wins times with an average of $black_mean"