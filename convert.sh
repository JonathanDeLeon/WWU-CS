PATHTOREPO=$1
IFS='/'; tokens=( $1 )
echo ${tokens[2]}
REPO=${tokens[2]}

git remote add other ${PATHTOREPO}
git fetch other
git checkout -b ${REPO} other/master
mkdir ${REPO}
git mv stuff ${REPO}/stuff                      # repeat as necessary for each file/dir
git commit -m "Added ${REPO}"
git checkout master
git merge ${REPO} --allow-unrelated-histories   # should add ZZZ/ to master
git commit
git remote rm other
git branch -d ${REPO}                           # to get rid of the extra branch before pushing
#git push                                    # if you have a remote, that is
