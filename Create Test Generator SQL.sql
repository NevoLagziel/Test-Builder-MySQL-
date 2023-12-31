Create schema TestGenerator;

USE TestGenerator;

CREATE table Question
(questionID INT NOT NULL,
questionTxt varchar(100),
questionType varchar(50),
primary key(questionID))
ENGINE = InnoDB;

CREATE table OpenQuestion
(QID INT NOT NULL,
theAnswer varchar(100),
primary key(QID),
foreign key(QID) references Question(questionID))
ENGINE = InnoDB;

CREATE table MultipleChoiceQuestion
(QID INT NOT NULL,
numOfAnswers int,
primary key(QID),
foreign key(QID) references Question(questionID))
ENGINE = InnoDB;

CREATE table Answer
(QID INT NOT NULL,
answerTxt varchar(100) NOT NULL,
isItTrue boolean,
primary key(QID,answerTxt),
foreign key(QID) references MultipleChoiceQuestion(QID))
ENGINE = InnoDB;

CREATE table Test
(testName varchar(50),
testID INT NOT NULL,
numOfQuestions int,
primary key(testID))
ENGINE = InnoDB;

CREATE table QuestionsInTest
(TID INT NOT NULL,
QID INT NOT NULL,
primary key(TID,QID),
foreign key (QID) references Question(questionID),
foreign key (TID) references Test(testID))
ENGINE = InnoDB;

CREATE table AnswerForMCQuestionInTest
(TID INT NOT NULL,
QID INT NOT NULL,
answerTxt varchar(100) NOT NULL,
isItTrue boolean,
primary key(TID,QID,answerTxt),
foreign key (QID,TID) references QuestionsInTest(QID,TID))
ENGINE = InnoDB;