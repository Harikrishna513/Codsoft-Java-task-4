import java.util.*;

public class QuizApplication {
    private List<Question> quizQuestions;
    private Map<Question, String> userAnswers;
    private int currentQuestionIndex;
    private int score;
    private Timer timer;

    public QuizApplication() {
        this.quizQuestions = new ArrayList<>();
        this.userAnswers = new HashMap<>();
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.timer = new Timer();
    }

    public void addQuestion(Question question) {
        quizQuestions.add(question);
    }

    public void startQuiz() {
        Collections.shuffle(quizQuestions);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                submitAnswer(null);
            }
        }, quizQuestions.get(currentQuestionIndex).getTimeLimitInSeconds() * 1000);

        takeNextQuestion();
    }

    public void takeNextQuestion() {
        if (currentQuestionIndex >= quizQuestions.size()) {
            endQuiz();
        } else {
            Question question = quizQuestions.get(currentQuestionIndex);
            System.out.println("Question: " + question.getQuestionText());
            System.out.println("Choices:");

            List<String> choices = question.getChoices();
            for (int i = 0; i < choices.size(); i++) {
                System.out.println((i + 1) + ". " + choices.get(i));
            }

            System.out.print("Enter your choice: ");
            try (Scanner scanner = new Scanner(System.in)) {
                String answer = scanner.nextLine();

                submitAnswer(answer);
            }
        }
    }

    public void submitAnswer(String answer) {
        timer.cancel();

        Question question = quizQuestions.get(currentQuestionIndex);
        userAnswers.put(question, answer);

        if (question.isCorrectAnswer(answer)) {
            score++;
        }

        currentQuestionIndex++;

        takeNextQuestion();
    }

    public void endQuiz() {
        System.out.println("Quiz completed!");

        System.out.println("\nScore: " + score + "/" + quizQuestions.size());

        System.out.println("\nSummary:");

        for (Question question : quizQuestions) {
            System.out.println("Question: " + question.getQuestionText());
            System.out.println("Your answer: " + userAnswers.get(question));
            System.out.println("Correct answer: " + question.getCorrectAnswer());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        QuizApplication quizApp = new QuizApplication();

        Question question1 = new Question("What is the capital of France?",
                Arrays.asList("A. London", "B. Paris", "C. Rome", "D. Madrid"),
                "B. Paris",
                10,
                "Easy");
        quizApp.addQuestion(question1);

        Question question2 = new Question("What is the largest planet in our solar system?",
                Arrays.asList("A. Earth", "B. Jupiter", "C. Saturn", "D. Mars"),
                "B. Jupiter",
                15,
                "Medium");
        quizApp.addQuestion(question2);

        Question question3 = new Question("Who painted the Mona Lisa?",
                Arrays.asList("A. Leonardo da Vinci", "B. Vincent van Gogh", "C. Pablo Picasso", "D. Claude Monet"),
                "A. Leonardo da Vinci",
                20,
                "Hard");
        quizApp.addQuestion(question3);

        quizApp.startQuiz();
    }
}

class Question {
    private String questionText;
    private List<String> choices;
    private String correctAnswer;
    private int timeLimitInSeconds;
    private String difficultyLevel;

    public Question(String questionText, List<String> choices, String correctAnswer,
            int timeLimitInSeconds, String difficultyLevel) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
        this.timeLimitInSeconds = timeLimitInSeconds;
        this.difficultyLevel = difficultyLevel;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrectAnswer(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }

    public int getTimeLimitInSeconds() {
        return timeLimitInSeconds;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }
}
