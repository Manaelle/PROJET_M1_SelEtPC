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
echo "-v : affichage de la version.\n "

echo "-p : test de la syntaxe (parsing)."
echo "-t : test du typage. \n"

echo "-kn : test de la K-normalisation."
echo "-ac : test pour A-conversion."
echo "-lr : test pour Let-reduction. \n"

echo "-asml : test de conversion en fichier .asml."
echo "-o : test de conversion en fichier .s (assembleur). \n"

echo "-q : quitter"

read mode

while [ "$mode" != "q" ] && [ "$mode" != "-q" ]
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
							echo "-br : test pour Let-reduction."

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
							rm test.txt
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
							rm test.txt
							;;
							
"-kn" | "kn")   echo '\n----------------TEST K-NORM ------------------'
							printf "\n \033[90m TEST DE LA K-NORMALISTION [CAS VALIDE]: \033[0m \n"

							#vérification de la différence entre fichier.ml et fichier.knorm.ml 
							for test_case in tests/knorm/valid/*.ml
							do	
									name_file=$(echo $test_case | cut -d'/' -f4 | cut -d'.' -f1 )
									name_file_knorm=tests/knorm/valid_correct/$name_file.knorm
									
									$MINCAMLC "-kn" $test_case  1>test.txt 2>/dev/null #knorm sera donc le résultat donnée par mincamlc
									
									#extraction du code dans knorm.txt
									sed '1d' test.txt > knorm.txt
									
									
									if diff -q $name_file_knorm knorm.txt 1>/dev/null;
									then		 
											 printf "%-30s : \033[32m [OK] \033[0m \n" $name_file
									else 
											printf "%-30s : \033[31m [ERROR] \033[0m \n" $name_file
											printf "\033[34m CODE OBTENU\033[0m \n" 
											cat knorm.txt
											
											printf "\033[34m CODE ATTENDU\033[0m \n" 
											cat $name_file_knorm
									fi
							done
							echo '\n---------------FIN DES TESTS------------------ \n'
							rm knorm.txt
							rm test.txt
							;;
"-ac" | "ac")   echo '\n----------------TEST A-CONV ------------------'
							printf "\n \033[90m TEST DE LA KA-CONVERSION [CAS VALIDE]: \033[0m \n"

							#vérification de la différence entre fichier.ml et fichier.knorm.ml 
							for test_case in tests/aconv/valid/*.ml
							do	
									name_file=$(echo $test_case | cut -d'/' -f4 | cut -d'.' -f1 )
									name_file_knorm=tests/aconv/valid_correct/$name_file.aconv
									
									$MINCAMLC "-ac" $test_case  1>test.txt 2>/dev/null #knorm sera donc le résultat donnée par mincamlc
									
									#extraction du code dans knorm.txt
									sed '1d' test.txt > aconv.txt
									
									
									if diff -q $name_file_knorm aconv.txt 1>/dev/null;
									then		 
											 printf "%-30s : \033[32m [OK] \033[0m \n" $name_file
									else 
											printf "%-30s : \033[31m [ERROR] \033[0m \n" $name_file
											printf "\033[34m CODE OBTENU\033[0m \n" 
											cat aconv.txt
											
											printf "\033[34m CODE ATTENDU\033[0m \n" 
											cat $name_file_knorm
											printf "\n"
									fi
							done
							echo '\n---------------FIN DES TESTS------------------ \n'
							rm aconv.txt
							rm test.txt
							;;

"-lr" | "lr")   echo '\n----------------TEST LET-RED ------------------'
							printf "\n \033[90m TEST DE LA LET-REDUCTION [CAS VALIDE]: \033[0m \n"

							#vérification de la différence entre fichier.ml et fichier.knorm.ml 
							for test_case in tests/lred/valid/*.ml
							do	
									name_file=$(echo $test_case | cut -d'/' -f4 | cut -d'.' -f1 )
									name_file_knorm=tests/lred/valid_correct/$name_file.lred
									
									$MINCAMLC "-lr" $test_case  1>test.txt 2>/dev/null #knorm sera donc le résultat donnée par mincamlc
									
									#extraction du code dans knorm.txt
									sed '1d' test.txt > lred.txt
									
									
									if diff -q $name_file_knorm lred.txt 1>/dev/null;
									then		 
											 printf "%-30s : \033[32m [OK] \033[0m \n" $name_file
									else 
											printf "%-30s : \033[31m [ERROR] \033[0m \n" $name_file
											printf "\033[34m CODE OBTENU\033[0m \n" 
											cat lred.txt
											
											printf "\033[34m CODE ATTENDU\033[0m \n" 
											cat $name_file_knorm
									fi
							done
							echo '\n---------------FIN DES TESTS------------------ \n'
							rm lred.txt
							rm test.txt
							;;
"-asml" | "asml")   echo '\n----------------TEST ASML ------------------'
							printf "\n \033[90m TEST DE ASML [CAS VALIDE]: \033[0m \n"

							#vérification de la différence entre fichier.ml et fichier.knorm.ml 
							for test_case in tests/asml/valid/*.ml
							do	
									name_file=$(echo $test_case | cut -d'/' -f4 | cut -d'.' -f1 )
									name_file_knorm=tests/asml/valid_correct/$name_file.asml
									
									$MINCAMLC "-asml" $test_case  1>test.txt 2>/dev/null #knorm sera donc le résultat donnée par mincamlc
									
									#extraction du code dans knorm.txt
									sed '1d' test.txt > asml.txt
									
									
									if diff -q $name_file_knorm asml.txt 1>/dev/null;
									then		 
											 printf "%-30s : \033[32m [OK] \033[0m \n" $name_file
									else 
											printf "%-30s : \033[31m [ERROR] \033[0m \n" $name_file
											printf "\033[34m CODE OBTENU\033[0m \n" 
											cat asml.txt
											
											printf "\033[34m CODE ATTENDU\033[0m \n" 
											cat $name_file_knorm
									fi
							done
							echo '\n---------------FIN DES TESTS------------------ \n'
							rm asml.txt
							rm test.txt
							;;							
							
"-o" | "o")   echo '\n----------------TEST ARM ------------------'
							printf "\n \033[90m TEST DE ARM [CAS VALIDE]: \033[0m \n"

							#vérification de la différence entre fichier.ml et fichier.knorm.ml 
							for test_case in tests/arm/valid/*.asml
							do	
									name_file=$(echo $test_case | cut -d'/' -f4 | cut -d'.' -f1 )
									name_file_knorm=tests/arm/valid_correct/$name_file.s
									
									$MINCAMLC "-o" $test_case  1>test.txt 2>/dev/null #knorm sera donc le résultat donnée par mincamlc
									
									#extraction du code dans knorm.txt
									sed '1d' test.txt > arm.txt
									
									
									if diff -q $name_file_knorm arm.txt 1>/dev/null;
									then		 
											 printf "%-30s : \033[32m [OK] \033[0m \n" $name_file
									else 
											printf "%-30s : \033[31m [ERROR] \033[0m \n" $name_file
											printf "\033[34m CODE OBTENU\033[0m \n" 
											cat arm.txt
											
											printf "\033[34m CODE ATTENDU\033[0m \n" 
											cat $name_file_knorm
									fi
							done
							echo '\n---------------FIN DES TESTS------------------ \n'
							rm arm.txt
							rm test.txt
							;;							
							
	*) 					echo "Commande invalide, veuillez consulter l'aide pour en savoir plus."
							;;
							
	esac
	echo "Veuillez ressaissir un mode ou quitter :"
	read mode
done




