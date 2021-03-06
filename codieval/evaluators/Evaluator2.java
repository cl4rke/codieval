package codieval.evaluators;

import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

import java.nio.charset.*;
import java.nio.file.*;

import codieval.exceptions.CompilationErrorException;
import codieval.problem.*;
import codieval.ucompiler.UniversalCompiler;
import codieval.hasher.Hasher;
import codieval.complexity.*;

public class Evaluator2 extends JFrame {

Font myFont1 = new Font("Verdana", Font.BOLD, 20);
Font myFont2 = new Font("Verdana", Font.BOLD, 16);
Font myFont3 = new Font("SansSerif", Font.BOLD, 13);
Font myFont4 = new Font("Consolas", Font.BOLD, 14);

JButton btnSampleInputFile = new JButton("Export sample input file");
JButton btnInputFile = new JButton("Export input file");
JButton btnOutputFile = new JButton("Import source code");
JButton btnSubmit = new JButton("Submit");
JButton btnRequirements = new JButton("More Information");

final JTextArea txtEvaluation = new JTextArea(20, 20);
final JTextField txtOutputFile = new JTextField(20);
final JTextArea txtDesc = new JTextArea(20, 20);

ArrayList problems;
JList listProblems = new JList();

JScrollPane scrlEvaluation = new JScrollPane(txtEvaluation);
JScrollPane scrlProblems;
JScrollPane scrlDesc = new JScrollPane(txtDesc, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

JPanel pnlDescOptions = new JPanel();
JPanel pnlDesc = new JPanel();
JPanel pnlOptions = new JPanel();
JPanel pnlSubmit = new JPanel();
JPanel pnlProblems = new JPanel();
JPanel pnlTimer = new JPanel();
JPanel pnlWest = new JPanel();
JPanel pnlNorth = new JPanel();
JPanel pnlCentr = new JPanel();
JPanel pnlSouth = new JPanel();
JPanel pnlEvaluation = new JPanel();
JPanel pnlLanguage = new JPanel();
JPanel pnlCompiler = new JPanel();

JFileChooser chooser = new JFileChooser();
private File selectedFile;

JLabel lblLanguage = new JLabel("Choose your language: ");
JLabel lblTime = new JLabel("PROBLEMS [ Timer: " + getTimeForHumans(0) + " ]");

JLabel lblCompiler = new JLabel(UniversalCompiler.getCompiler("C"));

int seconds = 0;

Timer timer = new Timer(1000, new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		seconds++;
		lblTime.setText("PROBLEMS [ Timer: " + getTimeForHumans(seconds) + " ]");
	}
});

public Evaluator2(String eventName, final boolean competition) {
	super("CODE EVALUATOR " + (!eventName.equals("")?"[ "+eventName+" ]":"") + (competition?" (COMPETITION MODE)":""));

	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

	setSize(1000, 600);
	setResizable(true);

	setLayout(new BorderLayout());

	txtEvaluation.setEditable(false);
	txtDesc.setEditable(false);
	txtOutputFile.setForeground(Color.BLUE);
	txtOutputFile.setEditable(false);

	pnlWest.setLayout(new BorderLayout());
	pnlProblems.setLayout(new GridLayout(1, 1));
	pnlTimer.setLayout(new FlowLayout());
	pnlLanguage.setLayout(new FlowLayout());
	pnlNorth.setLayout(new GridLayout(1, 1));
	pnlCentr.setLayout(new GridLayout(2, 1));
	pnlSubmit.setLayout(new FlowLayout());
	pnlDescOptions.setLayout(new BorderLayout());
	pnlDesc.setLayout(new GridLayout(1, 1));
	pnlOptions.setLayout(new FlowLayout());
	pnlEvaluation.setLayout(new GridLayout(1, 1));
	pnlSouth.setLayout(new BorderLayout());

	problems = new ArrayList();

	ArrayList<String> problemsSet = new ArrayList<String>();

	try {
		BufferedReader br = new BufferedReader(new FileReader(new File("settings/problems-set.txt")));

		for(String line = br.readLine(); line != null; line = br.readLine())
			problemsSet.add(line);

		br.close();
	}
	catch(IOException e) {
		e.printStackTrace();
	}

	for (int i = 1; ; i++) {
		String name = String.format("problem%04d", i);
		File file = new File("problems/"+name);

		if( ! file.exists())
			break;

		if(file.isDirectory()) {
			try {
				ArrayList<String> fileContents = new ArrayList<String>();

				BufferedReader br = new BufferedReader(new FileReader(new File("problems/"+name+"/desc.germ")));

				for(String line = br.readLine(); line != null; line = br.readLine())
					fileContents.add(line);

				br.close();
				Problem problem = new Problem(FileSystems.getDefault().getPath("problems/"+name, "input.txt"), fileContents);

				if(problemsSet.size() > 0) {
					for(String enabledProblem : problemsSet) {
						if(enabledProblem.equals(problem.title)) {
							problem.enabled = true;
							break;
						}
					}
				}
				else {
					problem.enabled = true;
				}

				if(problem.enabled)
					problems.add(problem);
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	listProblems = new JList(problems.toArray());
	listProblems.setSelectedIndex(0);

	txtDesc.setLineWrap(true);
	txtDesc.setText(((Problem)listProblems.getSelectedValue()).getDataString());
	txtDesc.setCaretPosition(0);

	scrlProblems = new JScrollPane(listProblems);

	pnlProblems.add(scrlProblems);

	pnlTimer.add(lblTime);

	pnlWest.add(pnlTimer, BorderLayout.NORTH);
	pnlWest.add(pnlProblems, BorderLayout.CENTER);

	btnSubmit.setEnabled(false);

	pnlSubmit.add(btnOutputFile);
	pnlSubmit.add(txtOutputFile);
	pnlSubmit.add(btnSubmit);

	pnlNorth.add(pnlSubmit);

	pnlDesc.add(scrlDesc);

	pnlEvaluation.add(scrlEvaluation);

	pnlCompiler.add(lblCompiler);

	pnlSouth.add(pnlNorth, BorderLayout.NORTH);
	pnlSouth.add(pnlEvaluation, BorderLayout.CENTER);
	pnlSouth.add(pnlCompiler, BorderLayout.SOUTH);

	pnlOptions.add(btnRequirements);
	pnlOptions.add(btnSampleInputFile);
	pnlOptions.add(btnInputFile);

	pnlDescOptions.add(pnlDesc, BorderLayout.CENTER);
	pnlDescOptions.add(pnlOptions, BorderLayout.SOUTH);

	pnlCentr.add(pnlDescOptions);
	pnlCentr.add(pnlSouth);

	add(pnlWest, BorderLayout.WEST);
	add(pnlCentr, BorderLayout.CENTER);

	pnlProblems.setBackground(Color.LIGHT_GRAY);
	pnlNorth.setBackground(Color.LIGHT_GRAY);
	pnlCentr.setBackground(Color.LIGHT_GRAY);
	pnlEvaluation.setBackground(Color.LIGHT_GRAY);

	txtDesc.setFont(myFont4);
	txtEvaluation.setFont(myFont4);
	txtOutputFile.setFont(myFont3);

	btnSampleInputFile.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			chooser.setSelectedFile(new File("sample_input.txt"));
			chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".txt")||f.isDirectory();
				}
				public String getDescription() {
					return "Textfile";
				}
			});
			if(chooser.showSaveDialog(Evaluator2.this) == JFileChooser.APPROVE_OPTION) {
				try {
					PrintWriter writer = new PrintWriter(new FileWriter(chooser.getSelectedFile()));
					writer.println(((Problem)listProblems.getSelectedValue()).sampleInput);
					writer.close();
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	});
	btnInputFile.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			chooser.setSelectedFile(new File("input.txt"));
			chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".txt")||f.isDirectory();
				}
				public String getDescription() {
					return "Textfile";
				}
			});
			if(chooser.showSaveDialog(Evaluator2.this) == JFileChooser.APPROVE_OPTION) {
				try {
					Files.copy(((Problem)listProblems.getSelectedValue()).inputFilePath, new FileOutputStream(chooser.getSelectedFile()));
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	});
	btnOutputFile.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent ae) {
			chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File f) {
					return true;
				}

				public String getDescription() {
					return "Source codes";
				}
			});

			int option = chooser.showOpenDialog(Evaluator2.this);
			if (option == JFileChooser.APPROVE_OPTION) {
				selectedFile = chooser.getSelectedFile();
				txtOutputFile.setText(selectedFile.getAbsolutePath());
				btnSubmit.setEnabled(true);
			}
		}
	});

	btnRequirements.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			setFocusableWindowState(false);

			Problem problem = ((Problem)listProblems.getSelectedValue());
			try {
				new Requirements(problem);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
			setFocusableWindowState(true);
		}
	});

	listProblems.addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			txtDesc.setText(((Problem)listProblems.getSelectedValue()).getDataString());
			txtDesc.setCaretPosition(0);
		}
	});

	btnSubmit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {

			try {
				ArrayList<String> realityLines = new ArrayList<String>();

				String dir = selectedFile.getParent();
				String filename = selectedFile.getName();

				for (String line : UniversalCompiler.compileAndRun(dir, filename).split("\n")) {
					realityLines.add(line);
				}

				Problem currentProblem = (Problem)listProblems.getSelectedValue();
				String result = currentProblem.expectedOutput.compare(realityLines, competition);
				txtEvaluation.setText(result);
				if(result.equals("OK!") && ! currentProblem.done) {
					seconds = 0;
					lblTime.setText("PROBLEMS [ Timer: " + getTimeForHumans(seconds) + " ]");
					currentProblem.done = true;
					listProblems.repaint();
				}
				if( ! competition) {
					java.util.List<String> sourceCodeLines = Files.readAllLines(
						selectedFile.toPath(), StandardCharsets.UTF_8);

					String sourceCode = "";
					for(String line : sourceCodeLines)
						sourceCode += line + "\n";

					BigO bigO = Complexity.getBigO(sourceCode);

					currentProblem.setCorrectness(currentProblem.expectedOutput.getCorrectness(realityLines)/2 + 50);
					currentProblem.setTimeComplexityGrade(bigO);
					currentProblem.computeGrade();

					txtEvaluation.setText(String.format(result+
						"\n String Difference: %.2f%% = %.2f"+
						"\n Time Complexity:   %s = %.2f%%" +
						"\n\n Overall Grade:   %.2f x 0.85 + %.2f x 0.15 = %.2f%% = %.2f",
						currentProblem.correctness, currentProblem.correctnessGrade,
						bigO, currentProblem.timeComplexityGrade,
						currentProblem.correctness, currentProblem.timeComplexityGrade,
						currentProblem.grade, currentProblem.equivalentGrade));

					listProblems.repaint();
				}
			}
			catch(IOException e) {
				txtEvaluation.setText("Error: Compiler not found");
				System.out.println(e.getMessage());
			}
			catch(CompilationErrorException error) {
				txtEvaluation.setText(error.toString());
			}
		}
	});

	timer.start();
	}

	public String getTimeForHumans(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = (seconds % 60);

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	public static void main(String args[]) {
		String eventName = "";
		boolean competition = false;

		if(args.length > 0) {
			eventName = args[0];
			if(args.length > 1) {
				competition = true;
			}
		}

		Evaluator2 sfc = new Evaluator2(eventName, competition);
		sfc.setVisible(true);
	}
}
