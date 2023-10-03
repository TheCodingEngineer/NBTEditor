#!/bin/bash

# Extract Sprite
for i in $(seq 0 3);
do
  for j in $(seq 0 3);
  do
    cmd="convert -extract 32x32+$(( $i * 32))+$(( $j * 32 )) resources/NBTSprite.png src/main/resources/images/${i}_${j}.png"
    echo $cmd
    $cmd
  done
done