# Perspective (Textured Entity Generator v0.1)
# Contributor(s): MCLegoMan
# Github: https://github.com/MCLegoMan/Perspective
# Licence: GNU LGPLv3

# This script will help you in the process of making textured entities for cats.
# Currently, this script will copy the input file and output a file for each variant.

# In future, this tool will ideally be updated to create the resource pack for you.
# I also want to add support for all vanilla entities (and possibly modded entities too if the mod supports Perspective)

import shutil
from pathlib import Path

print("Perspective: Textured Entity Generator v0.1")
print("Please Note: This script currently only supports cats.")
print("")

def createCat(tabby = True, black = True, red = True, siamese = True, british_shorthair = True, calico = True, persian = True, ragdoll = True, white = True, jellie = True, all_black = True):
    if (tabby == True): shutil.copy('./input.png', './' + name + '_tabby.png')
    if (black == True): shutil.copy('./input.png', './' + name + '_black.png')
    if (red == True): shutil.copy('./input.png', './' + name + '_red.png')
    if (siamese == True): shutil.copy('./input.png', './' + name + '_siamese.png')
    if (british_shorthair == True): shutil.copy('./input.png', './' + name + '_british_shorthair.png')
    if (calico == True): shutil.copy('./input.png', './' + name + '_calico.png')
    if (persian == True): shutil.copy('./input.png', './' + name + '_persian.png')
    if (ragdoll == True): shutil.copy('./input.png', './' + name + '_ragdoll.png')
    if (white == True): shutil.copy('./input.png', './' + name + '_white.png')
    if (jellie == True): shutil.copy('./input.png', './' + name + '_jellie.png')
    if (all_black == True): shutil.copy('./input.png', './' + name + '_all_black.png')

if Path("./input.png").is_file():
    name = input("Enter Name: ").lower()
    isValid = False
    while (isValid != True):
        isAllTypes = input("Create all types of cat? (y/n): ").lower()
        if (isAllTypes == "y" or isAllTypes == "yes"):
            createCat()
            isValid = True
        elif (isAllTypes == "n" or isAllTypes == "no"):
            isValid = True
            tabby = input("Tabby (y/n): ").lower()
            if (tabby == "y" or tabby == "yes"):
                tabby = True
            else:
                tabby = False
            black = input("Black (y/n): ").lower()
            if (black == "y" or black == "yes"):
                black = True
            else:
                black = False
            red = input("Red (y/n): ").lower()
            if (red == "y" or red == "yes"):
                red = True
            else:
                red = False
            siamese = input("Siamese (y/n): ").lower()
            if (siamese == "y" or siamese == "yes"):
                siamese = True
            else:
                siamese = False
            british_shorthair = input("British Shorthair (y/n): ").lower()
            if (british_shorthair == "y" or british_shorthair == "yes"):
                british_shorthair = True
            else:
                british_shorthair = False
            calico = input("Calico (y/n): ").lower()
            if (calico == "y" or calico == "yes"):
                calico = True
            else:
                calico = False
            persian = input("Persian (y/n): ").lower()
            if (persian == "y" or persian == "yes"):
                persian = True
            else:
                persian = False
            ragdoll = input("Ragdoll (y/n): ").lower()
            if (ragdoll == "y" or ragdoll == "yes"):
                ragdoll = True
            else:
                ragdoll = False
            white = input("White (y/n): ").lower()
            if (white == "y" or white == "yes"):
                white = True
            else:
                white = False
            jellie = input("Jellie (y/n): ").lower()
            if (jellie == "y" or jellie == "yes"):
                jellie = True
            else:
                jellie = False
            all_black = input("All Black (y/n): ").lower()
            if (all_black == "y" or all_black == "yes"):
                all_black = True
            else:
                all_black = False
            createCat(tabby, black, red, siamese, british_shorthair, calico, persian, ragdoll, white, jellie, all_black)
        else:
            print("Invalid Option!")
            isValid = False
else:
    print("Make sure the folder where this script is located contains your texture file as 'input.png' and try again!")
print("")
input("Press [ENTER] to close script!")
