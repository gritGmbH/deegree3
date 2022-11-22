#!/bin/bash

B=""
grep -e "-- branchlist --" -A 999 README.md | grep  -B 999 "endbranchlist" | grep "^  - " | tr ':' '/' | cut -d"-" -f 2- | while read branch
do
	# "${branch}" >> .project
	#	grep -q "${branch}" || break
	#B="${branch}"
	echo "Merging ${branch} ..."
	git merge "${branch}"
done
 
#test -z "${B}" || echo "Merging $B ..."
#test -z "${B}" || git merge $B 
#test -z "" && echo "Nothing to merge left"