#! /bin/sh
cd "$(dirname "$0")"/.. || exit 1

# TODO change this to point to your mincamlc executable if it's different, or add
# it to your PATH. Use the appropriate option to run the parser as soon
# as it is implemented
MINCAMLC=java/mincamlc
chmod +x $MINCAMLC

# run all test cases in syntax/valid and make sure they are parsed without error
# run all test cases in syntax/invalid and make sure the parser returns an error

# TODO extends this script to run test in subdirectories
# 
echo '\n------------------ TEST ----------------------'
echo 'Bienvenue à la phase de test. Pour commencer, selectionner le mode :'
echo "-h : affichage de l'aide"
echo "-v : affichage de la version."

echo "-p : test de la syntaxe (parsing)."
echo "-t : test du typage."

echo "-asml : test de conversion en fichier .asml."
echo "-o : test de conversion en fichier .s (assembleur)."

echo "-q : quitter"

read mode

while [ "$mode" != "q" ]
do
	case $mode in 
	"-h" | "h") echo '\n------------------ AIDE ----------------------'
							echo "-h : affichage de l'aide"
							echo "-v : affichage de la version."

							echo "-p : test de la syntaxe (parsing)."
							echo "-t : test du typage."
							
							echo "-kn : test de la K-normalisation."
							echo "-ar : test pour A-conversion."
							echo "-br : test pour B-reduction."

							echo "-asml : test de conversion en fichier .asml."
							echo "-o : test de conversion en fichier .s (assembleur)."
							;;
			
	"-v" | "v") echo "1.0.0"
							;;
						
	"-p" | "p") echo '\n---------------TEST SYNTAXE ------------------'
							printf "\n \033[90m TEST DE SYNTAXE [CAS VALIDE]: \033[0m \n"

							#si $MINCAMLC $test_case est redirigé vers STDERR (fichier test) : on test si test est vide ensuite 
							for test_case in tests/syntax/valid/*.ml
							do	
									name_file=$(echo $test_case | cut -d'/' -f4)
									$MINCAMLC "-p" $test_case  1>/dev/null 2>test.txt 
									if [ ! -s test.txt ] 
									then
											printf "%-30s : \033[32m [OK] \033[0m \n" $name_file 
									else 
											printf "%-30s : \033[31m [ERROR] \033[0m \n" $name_file
									fi
							done

							echo "\n \n \033[90m TEST DE SYNTAXE [CAS INVALIDE] \033[0m:"
							for test_case in tests/syntax/invalid/*.ml
							do	
									name_file=$(echo $test_case | cut -d'/' -f4)
									$MINCAMLC "-p" $test_case 1>/dev/null 2>test.txt
									if [ -s test.txt ] 
									then
											printf "%-30s : \033[32m [OK] \033[0m \n" $name_file
									else 
											printf "%-30s : \033[31m [ERROR] \033[0m \n" $name_file
									fi
							done
							echo '\n---------------FIN DES TESTS------------------ \n'
							;;

"-t" | "t") echo '\n---------------TEST TYPECHECKING ------------------'
							printf "\n \033[90m TEST DE TYPECHECKING [CAS VALIDE]: \033[0m \n"

							#si $MINCAMLC $test_case est redirigé vers STDERR (fichier test) : on test si test est vide ensuite 
							for test_case in tests/typechecking/valid/*.ml
							do	
									name_file=$(echo $test_case | cut -d'/' -f4)
									$MINCAMLC "-t" $test_case  1>/dev/null 2>test.txt 
									if [ ! -s test.txt ] 
									then
											printf "%-30s : \033[32m [OK] \033[0m \n" $name_file 
									else 
											printf "%-30s : \033[31m [ERROR] \033[0m \n" $name_file
									fi
							done

							echo "\n \n \033[90m TEST DE TYPECHECKING [CAS INVALIDE] \033[0m:"
							for test_case in tests/typechecking/invalid/*.ml
							do	
									name_file=$(echo $test_case | cut -d'/' -f4)
									$MINCAMLC "-t" $test_case  1>/dev/null 2>test.txt
									if [ -s test.txt ] 
									then
											printf "%-30s : \033[32m [OK] \033[0m \n" $name_file
									else 
											printf "%-30s : \033[31m [ERROR] \033[0m \n" $name_file
									fi
							done
							echo '\n---------------FIN DES TESTS------------------ \n'
							;;

	*) 					echo "Commande invalide, veuillez consulter l'aide pour en savoir plus."
							;;
	esac
	echo "Veuillez ressaissir un mode ou quitter :"
	read mode
done

rm test.txt


