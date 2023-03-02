import java.util.*;
import java.io.*;

public class QuizTree {
    private QuizTreeNode overallRoot; 

    public QuizTree(Scanner inputFile){
        List <String> quizList = new ArrayList<>();
        while (inputFile.hasNextLine()){
            quizList.add(inputFile.nextLine());
        }
        overallRoot = constructorHelper(quizList);
    }

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

    public void export(PrintStream outputFile){
        export(outputFile, overallRoot);
    }

    private void export(PrintStream outputFile, QuizTreeNode root) {
        if (root != null) {
            outputFile.println(root.data);
            export(outputFile, root.left);
            export(outputFile, root.right);
        }
    }     

    public void addQuestion(String toReplace, String leftChoice, String rightChoice, 
                        String leftResult, String rightResult){
        List <String> quizList = new ArrayList<>();
        quizList.add(leftChoice + "/" + rightChoice);
        quizList.add("END:"+ leftResult);
        quizList.add("END:"+ rightResult);
        QuizTreeNode newQuestion = constructorHelper(quizList);
        toReplace = "END:" + toReplace;
        replaceNode(toReplace.toLowerCase(), newQuestion, overallRoot);

    }

    private void replaceNode(String toFind, QuizTreeNode newNode, QuizTreeNode root){
        if (root.left != null && root.right != null){
            if (toFind.equals(root.left.data.toLowerCase())){
                root.left = newNode;
            }
            else if (toFind.equals(root.right.data.toLowerCase())){
                root.right = newNode;
            }
            else{
                replaceNode(toFind, newNode, root.left);
                replaceNode(toFind, newNode, root.left);
            }
        }
    }

    
    static class QuizTreeNode{
        public String data; 
        public QuizTreeNode left; 
        public QuizTreeNode right; 
    
        public QuizTreeNode(String data) {
                this(data, null, null);
        }
    
        public QuizTreeNode(String data, QuizTreeNode left, QuizTreeNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }
}

