Graphics 640, 480, 0, 2

	SeedRnd MilliSecs()	

	SetBuffer BackBuffer()

	Global gameOn = False
	Global endingScreen = False
	Global instructionsScreen = False
	Global background = LoadImage("backgroundFisherman1.png")
	Global rod= LoadImage("rodHook.png")
	Global background2 = LoadImage("backgroundFisherman2.png")
	Global fishImage1 = LoadImage("fish.png")
	Global fishImage2 = LoadImage("fish2.png")
	Global comboText = LoadImage("combo.png")
	Global xText = LoadImage("X.png")
	Global text1 = LoadImage("1.png")
	Global text2 = LoadImage("2.png")
	Global text3 = LoadImage("3.png")
	Global text4 = LoadImage("4.png")
	Global text5 = LoadImage("5.png")
	Global playButton = LoadImage("play.png")
	Global howToPlay = LoadImage("howToPlay.png")
	Global playDark = LoadImage("playDark.png")
	Global howToPlayDark = LoadImage("howToPlayDark.png")
	Global homeScreenImage = LoadImage("homeScreen.png")
	Global mainBackground = LoadImage("mainBackground.png")
	Global playAgain = LoadImage("playAgain.png")
	Global playAgainDark = LoadImage("playAgainDark.png")
	Global exitImage = LoadImage("exit.png")
	Global exitDark = LoadImage("exitDark.png")
	Global gameMusic = LoadSound("gameMusic.wav")
	Global popSound = LoadSound("pop.wav")
	Global rodY = -350
	Dim startDirection(9)
	Dim fishYArray(9)
	Dim fishXArray(9)
	Global SIZE = 9
	Global score = 0
	Global comboValue = 1
	Global combo = 0
	Global gameTimer = CreateTimer(1)
	Global musicTimer = CreateTimer(1)
	Global max = 30
	Global playSelected = True
	Global instructionsSelected = False
	Global playX = 132
	Global playY = 100
	Global instructionsX = 5000
	Global instructionsY = 0
	Global exitSelected = False
	Global playAgainSelected = False
	Global exitX = 5000
	Global exitY = 0
	Global playAgainX = 5000
	Global playAgainY = 0
	Global highScore = 0
	Global fnt30 = LoadFont("Times New Roman",30,False,False,False)
	Global fnt60 = LoadFont("Times New Roman",60,False,False,False)

	SoundVolume gameMusic,.05
	PlaySound gameMusic

	While Not KeyHit(16)

		While (Not KeyHit(1)) And gameOn = False And instructionsScreen = False And endingScreen = False
			Cls
			PlayGameMusic()
			DrawHomeScreen()
			DrawMenu()
			Flip
		Wend
	
	
		While (Not KeyHit(1)) And instructionsScreen = True
			Cls
			PlayGameMusic()
			DrawInstructions()
			Flip 
		Wend 
		

		FindFishLocationY()
	
		ResetTimer gameTimer

		While (Not KeyHit(1)) And gameOn = True
			Cls
			PlayGameMusic()
			InitFish()
			CalculateCombo()
			DrawImages()
			DrawScore()
			DrawGameTimer()
			FishMovment()
			RodMovement()
			CheckBounds()
			CheckIfHooked()
			CheckHighScore()
			Flip
		Wend


		exitSelected = False
		playAgainSelected = False
		exitX = 5000
		playAgainX = 5000

		While (Not KeyHit(1)) And endingScreen = True
			Cls
			PlayGameMusic()
			DrawEndingScreen()
			Flip
		Wend

	Wend
End


Function DrawImages()

	DrawImage(background,0,0)
	DrawImage(rod,0,rodY)
	DrawImage(background2,0,0)
	If comboValue >= 2 Then
		DrawImage(comboText,100,-8)
		DrawImage(xText,162,6)
	EndIf
	If comboValue = 2 DrawImage(text2,170,4)
	If comboValue = 3 DrawImage(text3,170,2)
	If comboValue = 4 DrawImage(text4,171,3)
	If comboValue = 5 DrawImage(text5,170,1)


End Function


Function RodMovement()

	If KeyDown(200) Then rodY = rodY - 5
	If KeyDown(208) Then rodY = rodY + 5

End Function


Function CheckBounds()

	If rodY <= -380 Then rodY = -380
	If rodY >= 0 Then rodY = 0

End Function


Function InitFish()

	For index = 0 To SIZE - 1
		If startDirection(index) = 0 Then 
			startDirection(index) = Rand(-1,300)
			If startDirection(index) > 1 Then startDirection(index) = 0
			If startDirection(index) = -1 Then fishXArray(index) = 640
			If startDirection(index) = 1 Then fishXArray(index) = -60
		EndIf 
	Next

End Function


Function FindFishLocationY()

	For index = 0 To SIZE - 1
		fishYArray(index) = Rand(150,400)
	Next

End Function


Function FishMovment()

	For index = 0 To SIZE - 1
		If fishXArray(index) >= -60 And fishXArray(index) <= 640
			If startDirection(index) = -1 Then DrawImage(fishImage1,fishXArray(index),fishYArray(index))
			If startDirection(index) = 1 Then DrawImage(fishImage2,fishXArray(index),fishYArray(index))
			fishXArray(index) = fishXArray(index) + 3 * startDirection(index)
		Else
			startDirection(index) = 0
		EndIf
	Next

End Function


Function CheckIfHooked()

	If KeyHit(57) Then
		hit = False
		For index = 0 To SIZE - 1
			If fishYArray(index) >= rodY + 400 And fishYArray(index) <= rodY + 470 And fishXArray(index) >= 170 And fishXArray(index) <= 235 Then
				startDirection(index) = 0
				fishXArray(index) = 0
				hit = True
				score = score + comboValue * 1 
			EndIf
		Next
		If hit = True Then
			combo = combo + 1

			PlaySound popSound
		Else
			combo = 0
		EndIf
	EndIf
	
End Function

Function DrawScore()

	SetFont fnt30
	Text 50,0,"Score: "+score,True,False

End Function


Function DrawGameTimer()

	time = TimerTicks(gameTimer)

	If time < 30 Then
		SetFont fnt30
		Text 550,0,max - time,True,False
	Else
		rodY = -350
		endingScreen = True
		gameOn = False
	EndIf

End Function


Function CalculateCombo()

	If combo = 0 Then comboValue = 1
	If combo = 5 Then comboValue = 2
	If combo = 6 Then comboValue = 3
	If combo = 7 Then comboValue = 4
	If combo = 8 Then comboValue = 5

End Function


Function DrawHomeScreen()

	DrawImage(homeScreenImage,0,0)
	DrawImage(playButton,-235,100)
	DrawImage(howToPlay,-110,310)

End Function


Function DrawMenu()

	DrawImage(playDark,playX,playY)
	DrawImage(howToPlayDark,instructionsX,instructionsY)
	
	If KeyHit(200) Then
		PlaySound popSound
		playX = 132
		playY = 100
		playSelected = True
		instructionsSelected = False
		instructionsX = 5000
	EndIf

	If KeyHit(208) Then
		PlaySound popSound
		instructionsX = -111
		instructionsY = 313
		instructionsSelected = True
		playSelected = False
		playX = 5000
	EndIf

	If KeyHit(57) Then
		PlaySound popSound
		If instructionsSelected = True Then instructionsScreen = True
		If playSelected = True Then gameOn = True
	EndIf

End Function


Function DrawInstructions()

	SetFont fnt30

	DrawImage(mainBackground,0,0)

	Text 330,50,"To move the rod use the UP and DOWN arrows.",True,False
	Text 330,70,"To catch a fish, press SPACE",True,False
	Text 330,110,"Each game is 30 seconds. Try to get the heighest score!",True,False
	Text 330,140,"Catch enough fish in a row to get a combo bonus.",True,False
	Text 330,170,"To close the game press Q+Esc",True,False
	Text 330,390,"Press SPACE to go back to MAIN MENU",True,False

	If KeyHit(57) Then instructionsScreen = False

End Function


Function DrawEndingScreen()

	DrawImage(mainBackground,0,0)
	DrawImage(exitImage,0,-13)
	DrawImage(playAgain,-10,-13)
	DrawImage(exitDark,exitX,exitY)
	DrawImage(playAgainDark,playAgainX,playAgainY)

	If KeyHit(203) Then
		PlaySound popSound
		exitX = -30
		exitY = -30
		exitSelected = True
		playAgainSelected = False
		playAgainX = 5000
	EndIf

	If KeyHit(205) Then
		PlaySound popSound
		playAgainX = 209
		playAgainY = -3
		playAgainSelected = True
		exitSelected = False
		exitX = 5000
	EndIf

	If KeyHit(57) Then
		PlaySound popSound
		If exitSelected = True Then
			score = 0
			combo = 0
			endingScreen = False
		EndIf
		If playAgainSelected = True Then
			score = 0
			combo = 0
			endingScreen = False
			gameOn = True
		EndIf
	EndIf

	SetFont fnt60
	Text 310,150,"YOUR SCORE: " + score,True,False

	SetFont fnt30
	Text 310,250,"High Score: " + highScore,True,False

End Function


Function CheckHighScore()

	If score > highScore Then highScore = score

End Function


Function PlayGameMusic()

	SoundVolume gameMusic,.05

	time = TimerTicks(musicTimer)

	If time = 94 Then
		PlaySound gameMusic
		ResetTimer musicTimer
	EndIf

End Function