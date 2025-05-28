const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const dotenv = require('dotenv');

dotenv.config();

const app = express();
app.use(cors({ origin: '*', credentials: true }));
app.use(express.json());

// ✅ MongoDB Atlas connection using .env variables
mongoose.connect(process.env.MONGO_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
})
  .then(() => console.log("✅ MongoDB connected"))
  .catch(err => console.error("❌ MongoDB connection error:", err));

// ✅ Quiz Schema
const QuizSchema = new mongoose.Schema({
  userId: { type: String, required: true },
  topic: { type: String, required: true },
  score: { type: Number, required: true },
  totalQuestions: { type: Number, required: true },
  correctAnswers: { type: Number, required: true },
  incorrectAnswers: { type: Number, required: true },
  date: { type: Date, default: Date.now }
});

const Quiz = mongoose.model('Quiz', QuizSchema);

// ✅ Routes
const router = express.Router();

// Health check
router.get('/', (req, res) => {
  res.send({ message: "🎉 Learning App API is live!" });
});

// Save quiz result
router.post('/quiz', async (req, res) => {
  try {
    const { userId, topic, score, totalQuestions, correctAnswers, incorrectAnswers } = req.body;

    if (!userId || !topic || totalQuestions == null || correctAnswers == null || incorrectAnswers == null) {
      return res.status(400).json({ success: false, message: 'Missing required fields' });
    }

    const quiz = new Quiz({ userId, topic, score, totalQuestions, correctAnswers, incorrectAnswers });
    await quiz.save();
    res.status(201).json({ success: true, message: "Quiz saved successfully" });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

// Get quiz history for a user
router.get('/quiz/:userId', async (req, res) => {
  try {
    const history = await Quiz.find({ userId: req.params.userId }).sort({ date: -1 });
    res.json(history);
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.use('/api', router);

// ✅ Start the server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`🚀 Server running on http://localhost:${PORT}`));
