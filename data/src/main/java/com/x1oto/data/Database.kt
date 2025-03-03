package com.x1oto.data

import com.x1oto.domain.model.Book
import com.x1oto.data.model.ReviewDTO
import com.x1oto.domain.utils.Genre

object Database {

    var books = mutableListOf(
        Book(
            1,
            "Kotlin in Action",
            "Dmitry Jemerov and Svetlana Isakova",
            Genre.PROGRAMMING,
            2017,
            5
        ),
        Book(
            2,
            "Kotlin Head First",
            "Dawn Griffiths and David Griffiths",
            Genre.PROGRAMMING,
            2021,
            2
        ),
        Book(3, "Grokking Algorithms", "Aditya Bhargava", Genre.ALGORITHMS, 2016, 3),
        Book(4, "The Hobbit", "J.R.R. Tolkien", Genre.FANTASY, 1937, 7),
        Book(5, "Pride and Prejudice", "Jane Austen", Genre.CLASSIC, 1813, 4),
        Book(6, "To Kill a Mockingbird", "Harper Lee", Genre.CLASSIC, 1960, 6),
        Book(7, "The Great Gatsby", "F. Scott Fitzgerald", Genre.CLASSIC, 1925, 8),
        Book(8, "Brave New World", "Aldous Huxley", Genre.DYSTOPIAN, 1932, 5),
        Book(9, "The Alchemist", "Paulo Coelho", Genre.PHILOSOPHICAL, 1988, 9),
        Book(10, "Crime and Punishment", "Fyodor Dostoevsky", Genre.CLASSIC, 1866, 12),
        Book(11, "Effective Java", "Joshua Bloch", Genre.PROGRAMMING, 2018, 6),
        Book(12, "Clean Code", "Robert C. Martin", Genre.PROGRAMMING, 2008, 9),
        Book(
            13,
            "Algorithms to Live By",
            "Brian Christian and Tom Griffiths",
            Genre.ALGORITHMS,
            2016,
            4
        ),
        Book(14, "The Fellowship of the Ring", "J.R.R. Tolkien", Genre.FANTASY, 1954, 10),
        Book(15, "Emma", "Jane Austen", Genre.CLASSIC, 1815, 5),
        Book(16, "Sense and Sensibility", "Jane Austen", Genre.CLASSIC, 1811, 4),
        Book(17, "1984", "George Orwell", Genre.DYSTOPIAN, 1949, 8),
        Book(18, "The Road", "Cormac McCarthy", Genre.DYSTOPIAN, 2006, 7),
        Book(19, "Meditations", "Marcus Aurelius", Genre.PHILOSOPHICAL, 180, 5),
        Book(20, "War and Peace", "Leo Tolstoy", Genre.CLASSIC, 1869, 13),
        Book(21, "Anna Karenina", "Leo Tolstoy", Genre.CLASSIC, 1877, 11),
        Book(22, "The Two Towers", "J.R.R. Tolkien", Genre.FANTASY, 1954, 10),
        Book(23, "The Return of the King", "J.R.R. Tolkien", Genre.FANTASY, 1955, 12),
        Book(24, "Les Misérables", "Victor Hugo", Genre.CLASSIC, 1862, 10),
        Book(25, "The Brothers Karamazov", "Fyodor Dostoevsky", Genre.CLASSIC, 1880, 14),
        Book(26, "Man's Search for Meaning", "Viktor E. Frankl", Genre.PHILOSOPHICAL, 1946, 9),
        Book(27, "The Catcher in the Rye", "J.D. Salinger", Genre.CLASSIC, 1951, 6),
        Book(28, "The Silmarillion", "J.R.R. Tolkien", Genre.FANTASY, 1977, 8),
        Book(29, "Inferno", "Dante Alighieri", Genre.PHILOSOPHICAL, 1320, 10),
        Book(30, "Algorithm Design Manual", "Steven S. Skiena", Genre.ALGORITHMS, 2008, 7)
    )

   val randomBooks = mutableListOf(
       Book(31, "THE GREAT GATSBY", "F. Scott Fitzgerald", Genre.CLASSIC, 1925, 8),
       Book(32, "THE CATCHER IN THE RYE", "J.D. Salinger", Genre.CLASSIC, 1951, 6),
       Book(33, "ALGORITHMS UNLOCKED", "Thomas H. Cormen", Genre.ALGORITHMS, 2013, 5),
       Book(34, "THE DARK TOWER", "Stephen King", Genre.FANTASY, 1982, 7),
       Book(35, "THE ART OF COMPUTER PROGRAMMING", "Donald E. Knuth", Genre.PROGRAMMING, 1968, 9),
       Book(36, "THE MATRIX AND THE MIND", "Robert M. Pirsig", Genre.PHILOSOPHICAL, 2006, 4),
       Book(37, "MEDITATIONS", "Marcus Aurelius", Genre.PHILOSOPHICAL, 180, 5),
       Book(38, "THE ROAD LESS TRAVELLED", "M. Scott Peck", Genre.PHILOSOPHICAL, 1978, 8),
       Book(39, "THE LORD OF THE RINGS", "J.R.R. Tolkien", Genre.FANTASY, 1954, 12)
    )

    val reviewsMap = mutableMapOf(
        1L to listOf(
            ReviewDTO("BookLover92", 4.5, "Чудова книга! Дуже сподобався стиль автора і глибина персонажів."),
            ReviewDTO("AlexReads", 3.8, "Цікаво, але трохи затягнуто. Друга половина книги здалася кращою.")
        ),
        3L to listOf(
            ReviewDTO("FantasyFan", 5.0, "Це було неймовірно! Атмосфера, сюжет – усе на найвищому рівні."),
            ReviewDTO("MysteriousReader", 5.0, "Без сумніву, одна з найкращих книг, які я читав цього року."),
            ReviewDTO("NightOwl", 4.7, "Захопило з перших сторінок! Неможливо було відірватися.")
        ),
        5L to listOf(
            ReviewDTO("JaneDoe", 2.5, "Очікувала більшого. Герої не викликали емоцій, фінал передбачуваний.")
        ),
        7L to listOf(
            ReviewDTO("ReaderX", 3.0, "Непогано, але нічого особливого. Книга на один раз."),
            ReviewDTO("LitCritic", 4.2, "Гарно написано, але хотілося б більше глибини у деяких сюжетних лініях.")
        ),
        10L to listOf(
            ReviewDTO("SciFiGeek", 4.8, "Фантастика на високому рівні! Відмінна наукова база."),
            ReviewDTO("CasualReader", 3.5, "Легка та приємна книга, але не вистачило чогось особливого.")
        ),
        12L to listOf(
            ReviewDTO("MysteryLover", 4.3, "Детективна історія з неочікуваним фіналом!"),
            ReviewDTO("RomanceReader", 3.9, "Мила історія кохання, але трохи передбачувана.")
        ),
        15L to listOf(
            ReviewDTO("ClassicLover", 4.6, "Справжня літературна класика, варта кожної хвилини.")
        ),
        18L to listOf(
            ReviewDTO("Philosopher", 4.8, "Глибока книга, яка змусила замислитися над життям.")
        ),
        20L to listOf(
            ReviewDTO("HistoricalLover", 4.9, "Історичні події відображені дуже правдоподібно."),
            ReviewDTO("PoetryFan", 4.3, "Чудові метафори, відчувається справжня душа автора.")
        ),
        24L to listOf(
            ReviewDTO("DeepThinker", 4.7, "Філософська ідея викладена дуже переконливо.")
        ),
        28L to listOf(
            ReviewDTO("SuspenseReader", 4.5, "Трилер тримав у напрузі до останньої сторінки!"),
            ReviewDTO("CulturalExplorer", 4.6, "Глибоке занурення у культуру іншої країни.")
        ),
        30L to listOf(
            ReviewDTO("AdventureFan", 5.0, "Епічна подорож, від якої захоплює дух!"),
            ReviewDTO("ComedyLover", 4.2, "Смішно і дотепно! Відмінний гумор.")
        )
    )
}