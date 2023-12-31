USE TestGenerator;

insert into Question values
(1,'Where are we learning?','OpenQuestion'),
(2,'What do cows drink?','MultipleChoiceQuestion'),
(3,'Which one is a type of food?','MultipleChoiceQuestion'),
(4,'What is our grade?','OpenQuestion'),
(5,'What is 2*2 ?','OpenQuestion'),
(6,'How are you today?','OpenQuestion');

insert into OpenQuestion values 
(1,'Afeka'),
(4,'Hopefully a 100'),
(5,'4'),
(6,'Fine');

insert into MultipleChoiceQuestion values
(2,5),
(3,6);

insert into Answer values
(2,'Coca cola',False),
(2,'Water',True),
(2,'Fnata',False),
(2,'Red bull',True),
(2,'Paper',False),
(3,'Phone',False),
(3,'Cookie',True),
(3,'Shoes',False),
(3,'Tower',False),
(3,'Pasta',True),
(3,'Glass',False);

insert into Test values
('2023-01-22',1,6),
('2023-01-22__1',2,5),
('2023-01-22__2',3,3);

insert into QuestionsInTest values
(1,1),
(1,2),
(1,3),
(1,4),
(1,5),
(1,6),
(2,2),
(2,6),
(2,4),
(2,5),
(2,1),
(3,3),
(3,5),
(3,6);

insert into AnswerForMCQuestionInTest values
(1,2,'Coca cola',False),
(1,2,'Water',True),
(1,2,'Fnata',False),
(1,2,'Red bull',True),
(1,2,'There are more than one right answer',True),
(1,2,'There are no right answers',False),
(1,3,'Phone',False),
(1,3,'Shoes',False),
(1,3,'Tower',False),
(1,3,'Pasta',True),
(1,3,'Glass',False),
(1,3,'There are more than one right answer',False),
(1,3,'There are no right answers',False),
(2,2,'Coca cola',False),
(2,2,'Water',True),
(2,2,'Fnata',False),
(2,2,'Paper',False),
(2,2,'There are more than one right answer',False),
(2,2,'There are no right answers',False),
(3,3,'Phone',False),
(3,3,'Shoes',False),
(3,3,'Tower',False),
(3,3,'Glass',False),
(3,3,'There are more than one right answer',False),
(3,3,'There are no right answers',True);