// Bhavesh Kumar
// 1 March, 2023
// CSE 123 BC with Ton
// Creative Assignment 3 - BrettFeed Quiz
import java.util.*;
import java.io.*;

// The QuizTree class represents the Quiz as a Binary Tree. This class contains
// the logistics behind setting up the quiz, exporting the quiz as a file, adding
// new questions to quiz and taking the quiz.
public class QuizTree {
    private QuizTreeNode overallRoot; 

    // The constructor method for Quiz sets up the Binary Tree for the quiz.
    // @params Takes in a scanner object which is the file inputted with the quiz and 
    // utilizes this file to create the overall tree. 
    // @returns A QuizTree object with the entire binary tree set up
    public QuizTree(Scanner inputFile){
        List <String> quizList = new ArrayList<>();
        while (inputFile.hasNextLine()){
            quizList.add(inputFile.nextLine());
        }
        overallRoot = constructorHelper(quizList);
    }

    // The constructorHelper is a private helper method which is used to create the respective
    // node to node relationship in the binary tree. 
    // @params Takes in a list of strings which contains the data of the binary tree and their 
    // relationship to the each other based on the location of the elements
    // @returns A QuizTreeNode object which is set with its respective data and relationships.
    private QuizTreeNode constructorHelper(List<String> quizList){
        QuizTreeNode root = new QuizTreeNode(quizList.remove(0));
        if (root.data.contains("END")){
            return root;
        }
        else {
            root.left = constructorHelper(quizList);
            root.right = constructorHelper(quizList);
            return root;
        }
    }

    // The takeQuiz method allows the user to take the current quiz as it prompts the user to
    // choose between the options to eventually print the result based on the made tree. If the
    // user inputs an option which is invalid an error message is printed and the user gets to 
    // try again until a correct option is choosen. The options are case insensitive.
    // @params Takes in a Scanner object which is connected to the console for the user to input
    // their choice.
    public void takeQuiz(Scanner console){
        QuizTreeNode curr = overallRoot;
        while (!curr.data.contains("END")){
            int divider = curr.data.indexOf("/");
            String option1 = curr.data.substring(0, divider).toLowerCase();
            String option2 = curr.data.substring(divider + 1, curr.data.length()).toLowerCase();
            System.out.print("Do you prefer " + option1 + " or " + option2 + "? ");
            String ans = console.nextLine().toLowerCase();
            if (ans.equals(option1)){
                curr = curr.left;
            }
            else if (ans.equals(option2)){
                curr = curr.right;
            }
            else{
                System.out.println("  Invalid choice. Please try again.");
            }
        }
        System.out.println("Your result is: " + curr.data.replace("END:", ""));
    }

    // The export function writes the current quiz structure to the given output file.
    // @params A PrintStream object which represents the output file the user wants the 
    // output to be written in. 
    public void export(PrintStream outputFile){
        export(outputFile, overallRoot);
    }

    // The export function writes the current quiz structure to the given output file.
    // @params A PrintStream object which represents the output file the user wants the 
    // output to be written in. 
    // @params a QuizTreeNode which is the root node that is currently being printed
    // on the given file.
    private void export(PrintStream outputFile, QuizTreeNode root) {
        if (root != null) {
            outputFile.println(root.data);
            export(outputFile, root.left);
            export(outputFile, root.right);
        }
    }     

    // The addQuestion method replaces the node with the given string with a new node
    // representing a new question with a left and right choice and their following results.
    // The Strings in this method are all case insesnitive. 
    // @params A string which represents the data of the node to be replaced. 
    // @params A string which represents the new left choice.
    // @params A string which represents the new right choice.
    // @params A string which represents the result of the left choice.
    // @params A string which represents the result of the right choice.
    // @exception Throws an IllegalArgumentException if the value of toReplace is not a 
    // end node in the Tree or is a choice node which is invalid. 
    public void addQuestion(String toReplace, String leftChoice, String rightChoice, 
                            String leftResult, String rightResult){
        
        List <String> quizList = new ArrayList<>();
        quizList.add(leftChoice + "/" + rightChoice);
        quizList.add("END:"+ leftResult);
        quizList.add("END:"+ rightResult);
        QuizTreeNode newQuestion = constructorHelper(quizList);
        toReplace = "END:" + toReplace;
        boolean [] found = new boolean[1];
        found [0] = false;
        replaceNode(toReplace.toLowerCase(), newQuestion, overallRoot, found);
        if (found[0] == false){
            throw new IllegalArgumentException("The string to replace is not valid");
        }
    }


    // The private method replaceNode treverses the tree insearch of the node which 
    // contains the given string and replaces that node with the given node. The strings
    // are all case insensitive. 
    // @params The string of node to be replaces.
    // @params A QuizTreeNode which is suppose to replace the node.
    // @params A QuizTreeNode which represents the root node of the Quiz tree being treversed.
    // @params a list of boolean which is size one and is set to true if the toFind string is an
    // end node of the tree. It is false if the string doesn't exist or is a option node.
    private void replaceNode(String toFind, QuizTreeNode newNode, QuizTreeNode root, boolean[] found){
        if (root.left != null && root.right != null){
            if (toFind.equals(root.left.data.toLowerCase())){
                root.left = newNode;
                found[0] = true;
            }
            else if (toFind.equals(root.right.data.toLowerCase())){
                root.right = newNode;
                found[0] = true;
            }
            else{
                replaceNode(toFind, newNode, root.left, found);
                replaceNode(toFind, newNode, root.right, found);
            }
        }        
    }

    // The QuizTreeNode class is used to represent a single node in the QuizTree.
    // Each node contains its own data and has a relationship with subnodes on the 
    // left and right side.
    static class QuizTreeNode{
        public String data; 
        public QuizTreeNode left; 
        public QuizTreeNode right; 
        
        // The constructor creates the QuizTreeNode object. 
        // @params A string object which represents the data of the node.
        public QuizTreeNode(String data) {
                this(data, null, null);
        }
        // The constructor creates the QuizTreeNode object which should contains a string data. 
        // @params A string object which represents the data of the node.
        // @params A QuizTreeNode which is set as the left branch of the current node.
        // @params A QuizTreeNode which is set as the right branch of the current node.
        public QuizTreeNode(String data, QuizTreeNode left, QuizTreeNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }
}

