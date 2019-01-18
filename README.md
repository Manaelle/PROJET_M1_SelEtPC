# Projet Compilation : Sel&PC

## Membres du groupe

* Guillaume CAILHE : Front-end (A-CONV)
* Nadia BENMOUSSA : Typechecking + Front-end (LET-RED)
* Faris BOULAKHSOUMI : Typechecking + Front-end (ASML)
* Florian ARGAUD : Front-end (KNORM)

* Fabien LEFEBVRE : Back-end
* Manal BENAISSA : Tests + typechecking + secrétariat

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


## Compilation

Exécuter make dans le dossier java/

> cd java/
> make

## Exécution

Script permettant les tests en série (les instructions suivantes sont donnés à l'exécution du script) :

> ./scripts/mincaml-test.sh

Exécution manuelle :

> ./java/mincamlc [-OPTION] [FICHIER A CONVERTIR]

Les options sont les mêmes que ceux du script. (Si il n'y a pas d'option, toutes les étapes seront exécutées) :

> -p : test de la syntaxe (parsing).
> -t : test du typage.
> -kn : test de la K-normalisation.
> -ac : test pour A-conversion.
> -lr : test pour Let-reduction.
> -asml : test de conversion en fichier .asml.
> -o : test de conversion en fichier .s (assembleur).

Les fichiers tests sont dans les dossiers valid/ ou invalid/ situés dans tests/.

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



