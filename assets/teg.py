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

def createFiles():
    shutil.copy('./input.png', './' + name + '_tabby.png')
    shutil.copy('./input.png', './' + name + '_black.png')
    shutil.copy('./input.png', './' + name + '_red.png')
    shutil.copy('./input.png', './' + name + '_siamese.png')
    shutil.copy('./input.png', './' + name + '_british_shorthair.png')
    shutil.copy('./input.png', './' + name + '_calico.png')
    shutil.copy('./input.png', './' + name + '_persian.png')
    shutil.copy('./input.png', './' + name + '_ragdoll.png')
    shutil.copy('./input.png', './' + name + '_white.png')
    shutil.copy('./input.png', './' + name + '_jellie.png')
    shutil.copy('./input.png', './' + name + '_all_black.png')

if Path("./input.png").is_file():
    name = input("Enter Name: ").lower()
    isValid = False
    while (isValid != True):
        isAllTypes = input("Create all types of cat? (y/n): ").lower()
        if (isAllTypes == "y"):
            createFiles()
            isValid = True
        elif (isAllTypes == "n"):
            isValid = True
            print("Variant specific texture generation is coming soon!")
            createFiles()
        else:
            print("Invalid Option!")
            isValid = False
else:
    print("Make sure the folder where this script is located contains your texture file as 'input.png' and try again!")
print("")
input("Press [ENTER] to close script!")
