CREATE TABLE Questions(
    QuestionID int NOT NULL PRIMARY KEY,
    IsAnswerList BOOL,
    QuestionText varchar(300)
);
CREATE TABLE ProjectRequst(
    RequestID int NOT NULL PRIMARY KEY,
    Requester_Name varchar(300),
    Budget_Amount DECIMAL(10, 2),
    Budget_Currency varchar(50)
);
CREATE TABLE ProjectSection(
    SectionID int NOT NULL PRIMARY KEY,
    Section_Title varchar(300),
    Section_Description varchar(300)
);
CREATE TABLE ProjectRequestAnswers(
    RequestID int NOT NULL,
    SectionID int NOT NULL,
    QuestionID int NOT NULL,
    AnswerValue varchar(500),
    FOREIGN KEY (RequestID) references ProjectRequst(RequestID),
    FOREIGN KEY (SectionID) references ProjectSection(SectionID),
    FOREIGN KEY (QuestionID) references Questions(QuestionID),
    CONSTRAINT RequestAnswerID PRIMARY KEY (RequestID, SectionID, QuestionID)
);