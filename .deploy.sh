#!/bin/bash
gitLastCommit=$(git show --summary --grep="Merge pull request")
if [[ -z "$gitLastCommit" ]]
then
	lastCommit=$(git log --format="%H" -n 1)
else
	echo "We got a Merge Request!"
	#take the last commit and take break every word into an array
	arr=($gitLastCommit)
	#the 5th element in the array is the commit ID we need. If git log changes, this breaks. :(
	lastCommit=${arr[4]}
fi
echo $lastCommit

filesChanged=$(git diff-tree --no-commit-id --name-only -r $lastCommit)
if [ ${#filesChanged[@]} -eq 0 ]; then
    echo "No files to update"
else
    for f in $filesChanged
	echo $f
	do
		#do not upload these files that aren't necessary to the site
		if [ "$f" == *'.php'* ] && [ "$f" != *'Test.'* ]
		then
	 		echo "Uploading $f"
	 		curl --ftp-create-dirs -T $f -u $FTP_USER:$FTP_PASS ftp://appxpired.winterapps.de/api2/$f
		fi
	done
fi
