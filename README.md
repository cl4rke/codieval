Code Evaluator
==============

This is a system for automatically evaluating student code with appropriate grades. It may also be used for programming competitions.

### Folder structure

	codieval/
		answers/
			problem1
				input.txt
				Problem1.java*
			problem2
				input.txt
				problem2.c*
			problem3
				input.txt
				problem3.py*
		codieval/
			admin/
				ProblemPanel.java
				ProblemsManager.java
			evaluators/
				Evaluator.java
				Evaluator2.java
			exceptions/
				CompilationErrorException.java
			hasher/
				Hasher.java
			problem/
				ExpectedOutput.java
				Problem.java
				Requirements.java
			ucompiler/
				UniversalCompiler.java
		problems/
			problem1
				desc.germ
				input.txt
			problem2
				desc.germ
				input.txt
			problem3
				desc.germ
				input.txt
		Codieval.jar
		README.md

__Note:__ The * means that naming the user's source codes and is up to the user.

## Installation

Clone this repo using:

	$ git clone http://github.com/arkeidolon/codieval

Install the following compilers (ignore the programming languages you don't need):

| Programming Languages | Compilers used by Codieval | Recommended download links |
| --------------------- | -------------------------- | -------------------------- |
| C		        | gcc			     | https://gcc.gnu.org/releases.html or http://sourceforge.net/projects/mingw/files/latest/download?source=files       |
| C#    	        | csc			     | http://msdn.microsoft.com/en-us/library/dd831853.aspx |
| Java		        | javac & java		     | http://www.oracle.com/technetwork/java/javase/downloads/index.html |
| Python	        | python		     | https://www.python.org/downloads/ |

Make sure to include the compilers' directories to the system PATH.

### How to compile

On the __codieval root folder__, run

	$ javac codieval/evaluators/Evaluator2.java

### How to run

After compiling, on the __codieval root folder__, run

	$ java codieval.evaluators.Evaluator2

### How to make jar file

After compiling, on the __codieval root folder__, run

	$ jar cfm codieval.jar manifest.mf codieval/*/*.class

### How to open admin interface

On the __codieval root folder__, compile and run codieval/admin/ProblemsManager.java:

	$ javac codieval/admin/ProblemsManager.java
	$ java codieval.admin.ProblemsManager

## User Manual

### Code Evaluator

You can choose whatever problem you want to do first. Once you do, you have various options.

#### More Information

Here you will see more information of the problem, including its required input and output.

#### Export sample input file / Export input file

Each problem has two types of input files: sample and actual. Sample input files are usually smaller than actual input files, as they are only used for experimentation by the users. You can view the contents of both input files by clicking the More Information button.

By clicking the Export buttons, the user will have the option to save the input files wherever they want. It is recommended that the user saves the input files in the same folder as their program for easier file handling.

#### Import source code

The user may import the main module of their program any time and test it using the Submit button.

#### Submit

Any errors for the source code will be shown in the text area below. If there are none, the system will continue to evaluate the code, with its output and its time complexity. Time complexity is calculated based on the depth of the for loops in the user's program. After evaluation, a grade based of the QPI grading system(1.0 - 5.0) will be shown at the problems list at the leftmost part of the dialog box, indicating the grade of the code submitted by the user. The timer will also reset to 0, indicating how much time has passed since the recent submission.

The user is free to try and submit again any time, and aim for a higher score.

### Admin Interface

After opening codieval.admin.ProblemsManager, you have control over the administration side of the system.

#### Choosing of Problems

You can check and uncheck problems depending on what you want to include in the Code Evaluator for current session. Unchecked problems will not be shown. When asked if you want to save changes upon exiting the Problems Manager, make sure to confirm with "Yes" to have your checked and unchecked problems be commited.

#### Searching of Problems

You can search for any problem by typing your query at the search bar and pressing Enter. Searching for "a" will result to all problems with a letter "a" on the title.

#### Viewing of Problem Information

You can view the problems' information by clicking the Info button before the title of each problem.

#### Adding of New Problems

Upon clicking the New Problem button, a new dialog box will be shown for the input of a new problem. Set its Title and Description, then import its sample input and actual input. Sample inputs are usually smaller than actual inputs. Then, import your own sample source code. The source code's file name should appear at the space to the left of the "Import source code" button, along with its time complexity. After confirming the label's contents, try to Generate the output, and if successful, the "Save" button will become enabled. Clicking the Save button will save your new problem, given that you already have filled up the Title and Description.

Once you have saved your problem, you might need to restart the Problems Manager in order for the new problem to show up in the problems list.

## Deployment

After making a jar file, delete the codieval/ folder containing all the source codes, to avoid the users from tinkering with the code and recompiling it, possibly changing the mechanics of the system.
