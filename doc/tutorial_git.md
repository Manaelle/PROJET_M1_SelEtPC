Git tutorial
============

Before starting this lab, you should be familiar with the basic concepts of Git
(repository, revision, commit, difference between distributed and centralized 
CVS). 
If not, refer to 
[book (English)](http://git-scm.com/book/en/v2/Getting-Started-Git-Basics) 
or to any git tutorial.

Configuration
-------------

Everyone must complete this part.

### ssh keys

Check that you have a directory `~/.ssh`. If not, run the command: 
`ssh-keygen` in your `$HOME` directory. At the question "Enter passphrase 
(empty for no passphrase):", you may hit return.

At this point, you own two cryptographic keys. A private key `~/.ssh/id_rsa`
and a public key `~/.ssh/id_rsa.pub` (or `id_dsa` and `id_dsa.pub`).

Reminder: Among other things, a couple public / private keys allows for the 
authentication of a user on a server. A user stores his public key on 
a remote server, then at login time, the server checks whether the public 
and private keys match. If they do, the user is authenticated without the 
need to enter a password.

Later on, you'll upload your public key on the server that hosts your shared git  repository.

### Git configuration: .gitconfig

If you don't have one already, create a file `~/.gitconfig` with the 
following content:

    [core] editor =  <your favorite editor (vim, emacs, gedit...)>
    [user] name = <your real name>
         email = <your email address>
    [push] default = current

### Setting up git repositories

For the remainder of the lab, you can team up with another student if you wish.

Before starting to work on the repository that you're going to share during 
the programming project, you're going to experiment with `git` using 
temporary local repositories.

Run the following commands:

    mkdir repo.git
    cd repo.git
    git init --bare
    cd ..
    git clone repo.git repo-A
    git clone repo.git repo-B


The directories `repot.git`, `repo-A` and `repo-B` are all (empty) git repositories.
`repo-A` and `repo-B` are clones of `repo.git`. A typical git setting 
consists of a shared repository on a server (i.e., `repo.git`), and several 
local repositories for each member of the group (i.e., `repo-A` and 
`repo-B`).

In this simpler setting, all the repositories are local but this is enough 
for us to understand a typical git workflow.

### Working locally

Run the following commands:

    cd Repo-A
    echo "hello" > a.txt
    git status

The `git status` command is very useful and you should run it often. It tells 
you the status of all your files and in particular what has changed since the 
last commit.  (a commit is a snapshot of your project at a given time). Here, 
`a.txt` is untracked: it's not stored in the repository and it won't be until 
you add it.

    git add a.txt
    git status

From now on, `a.txt` is tracked by git. It'll be added to the next commit.

    git commit

Git opens the text editor you specified in `~/.gitconfig` in order for the 
user to type a message describing the new revision of the system. After this 
  is done, a new revision of the system is created in the repository 
  `repo-A`.

Remark: you can also use the following command:

    git commit -m "description of the new revision"

Similarly, create, add and commit a file `b.txt`.
Now, update `a.txt` and see the new status.

    echo "good bye" >> a.txt
    git status

Although the file has been modified, it won't be added to the next commit 
unless marked explicitly as "staged". This is achieved with the command:

    git add a.txt

Remark: this is a second way to use `git add`.

We can then create a new revision with:

    git commit -m "a.txt updated with good bye"

Remark: We can do the "add" step directly in the commit command with `git commit -a`. With this
option, all the modified files are included in the new revision. We advise you to always
use this option until you feel more confident.

Other useful commands:

    git diff

shows the diff between the working directory and the last commit. Typically, it helps you
to review your changes before commiting. Is your code good-enough to be shared with
others?

    git log

shows the list of all commits. Notice that each commit is identified by a 40-letters
hashtag.

Some files shouldn't be included in the repository. Typically, binary or generated files such as:
*.pdf, *.o, *.class. They can be listed in a `.gitignore` file at the root of your repository.

For example, run the following commands:

    touch c.txt
    git status
    echo c.txt > .gitignore
    git status

You should see that git now ignores `c.txt`.

Finally, you should add the `.gitignore` file to the repository:

    git add .gitignore
    git commit -am "added gitignore"

### Synchronizing with a server

So far, all the commands only involved `repo-A`. In a distributed settings, those
commands can be performed offline.

In order to share your work with others, we need to "push" it to the shared
repository `repo.git`.

For that, run the command:
    git push

You can now pull all the new commits from repo-B with:
    cd ../repo-B
    git pull

At that point, `repo-A` and `repo-B` should look exactly the same. In 
particular, `ls` should show `a.txt b.txt`. You can also check with `git log` 
that all commits are the same in both repositories.

Now update the file `a.txt` in `repo-A`, then commit your change. And 
similarly, update the file `b.txt` in `repo-B`, then commit your change.

In `repo-A`, run:
    git push

Then in `repo-B`, run:
    git push

Normally, you should see error message. The reason is that B is trying to push its
change to the server while it has not pulled the latest available revision yet.
You need to first run:

    git pull

Fill the commit message, and check that `a.txt` has been updated. Then run:

    git push.

Finally, you can run in `repo-A`

    git pull

Now, `repo-A` and `repo-B` should be synchronized again.

Remark: `git pull` actually is a shortcut for two different operations.  The 
first operation is `git fetch` which retrieves all the commits on the server 
that it doesn't own yet. Then it runs `git merge` that tries to merge the 
latest local commit (sometimes referred to as `HEAD`) with the latest commit 
from the server (called `origin/master`). This merge operation created a new 
commit as a side effect.  You can check that with `git log`.

Here, the merge operation was very simple as the concurrent changes took 
place on different files. That's why git could do it automatically, but it's 
not always the case.

We recommend that you keep experimenting with similar changes before 
proceeding to the next part. Update a file in `repo-A`, commit and push your 
changes, pull the new commits from `repo-B` and so on. Do not update 
concurrently the same part of the same file in `repo-A` and `repo-B` as it 
will lead to conflicts that git isn't able to solve automatically.

### Dealing with conflicts

First, check that `repo.git`, `repo-A` and `repo-B` are fully synchronized.

In `repo-A` and `repo-B`, change the first line of `a.txt` in two different ways.  Then commit
your change (but don't push them) in both repository.

Then run in `repo-A`:
    git push

and in `repo-B`:
    git pull

This time, git wasn't able to merge the two commits and it has to be fixed manually. Both
versions appear in the file `a.txt` in a "diff" format similar to this.

    <<<<<<< HEAD
    changes made by B
    =======
    changes made by A
    >>>>>>> a1f77ea359168bcec48d9167627e4689bc93ac0c

The user has to go through all the changes and merge them correctly (i.e., manually solve
the conflicts).  Once this is done, the conflict should be marked as resolved with:

    git add a.txt

Remark: this is a third way of using git add.

Then, the commit can be finalized with:

    git commit

and finally, the new commit can be pushed on the server.

Then, go to `repo-A` and pull the new commit.

Now, the two repositories should be synchronized again.

We recommend that you repeat the previous steps as many times as needed until you feel
comfortable.

### Some advice

In order to avoid conflicts, communicate regularly with your teammates in 
order to know who works on what.

Regularly synchronize with the server `git push` and `git pull` in order to 
share your work, and retrieve the latest versions of the project. Before 
performing a pull operation, you must commit your change so that they are not 
lost during the merge process.

Don't forget to add the new files you've created (`git status` helps you for that).
This is a common mistake. When this happens, your project will compile 
locally but your teammates will have missing files that will prevent them 
from compiling the project.

Never commit something that doesn't compile.

Do not add binary or generated files to the repository.

In case of problem, `git stash` saves the current state of the project, and 
reverse to the last commit. It may not work in case of unmerged conflicts. In 
this case, you can use `git reset --hard`, but all your uncommited changes 
will be lost.

### IDEs

Most IDEs supports git natively or through the use of plugins. For instance, with Eclipse
you can use `egit`.

### References

* [Git official documentation](http://git-scm.com/documentation)
* [Pro git](http://git-scm.com/book/en/v2)
* A [git tutorial](http://www.vogella.com/articles/Git/article.html)
* An [`egit` tutorial](http://www.vogella.com/articles/EGit/article.html)

### Conclusion

Git is ubiquitous and sooner or later you will have to use it. This project
is a good opportunity for you to learn it. This lab only showed you enough  to
get started. You will need to learn from other resources for a more  advanced
usage. We recommend particularly the book "pro.git".
