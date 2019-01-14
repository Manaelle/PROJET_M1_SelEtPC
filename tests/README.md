# Tests

## Les tests disponibles

### Syntaxe/typechecking
* 'syntax/' : test de syntaxe (étape 0)
* 'typechecking/' : test du typechecking (étape 1)

### Frontend
* 'knorm/' : test de k-normalisation (étape 2)
* 'aconv/' : test de a-conversion (étape 3)
* 'bred/' : test de B-reduction (étape 4)
* 'lred/' : test de Let-reduction (étape 5)
* 'asml/' : test de asml (étape 6)

### Backend
* 'arm/' : test de asml (étape 7)

## Création des tests
### Syntaxe/typechecking
Il suffit d'ajouter des fichiers .ml dans 'valid/' (fichiers corrects) ou 'invalid/'

### Frontend
Les tests contiennent 2 dossiers distincts : 'valid/' et 'valid_correct/'.
Dans 'valid/', il faut mettre les fichiers 'TEST.ml' à tester. Dans 'valid_correct/', il faut mettre la réponse ATTENDUE correspondant au fichier 'TEST.ml'

**Attention**, les fichiers dans 'valid_correct/' doivent porter le même nom que les fichiers dans 'valid/' qu'ils testent, suivie de l'extension '.knorm', '.aconv', '.bred', '.lred' ou '.asml'

### Backend

Idem que frontend. Les fichiers dans 'valid_correct/' doivent porter l'extension '.s'. Les fichiers dans 'valid/' doivent porter l'extension '.asml'
