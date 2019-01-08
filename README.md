# Projet Compilation : Sel&PC

## Membres du groupe

* Guillaume CAILHE : Front-end
* Nadia BENMOUSSA : Front-end
* Faris BOULAKHSOUMI : Front-end + test
* Florian ARGAUD : Front-end + test

* Fabien LEFEBVRE : Back-end
* Manal BENAISSA : Back-end

## Installation (avec Netbeans)

* ETAPE 1 : Cloner le projet git
* ETAPE 2 : Ouvrir Netbeans. 
- Créer un nouveau projet : File > New Project > Java > Java Project with Existing Source. 
- Cliquer sur Next. 
- Donner un nom au projet ( "SelPC" pour moi). Mettez-le dans un dossier "SelPC" : LE PROJET DOIT ETRE A L'EXTERIEUR DU DOSSIER CLONE. 
- Cliquer sur Next.
- Dans "Add Folder", ajouter "PROJET_M1_SelEtPC/java"
- Cliquer sur Next.
- Inclure "*.java" (tous les fichiers javas) et exclure "*flex*" (tous les fichiers flex)
- Cliquer sur "Finish"

- Click-droit sur le projet "SelPC" > Propriétés
- Dans "Librairies", ajouter les deux librairies java-cup-11b.jar et java-cup-11b-runtime.jar
- Dans "Run", ajouter l'argument "mincaml/print.ml" (ou un autre fichier ml)
- faire un make sur la console (éventuellement donner les droits : chmod +x jflex/bin/jflex)
- fini !

## Test
### Liste de ce qu'il faut tester
#### Constantes
- int
- float
- bool
- unit
- var
- tuple
- array

#### Opérations arithmétiques
- neg
- add
- sub
- fneg
- fadd
- fsub
- fmul
- fdiv

#### Conditions & opérations booléennes
- not
- eq
- le
- if

#### Fonctions
- let
- letrec
- app
- lettuple
- get
- put



