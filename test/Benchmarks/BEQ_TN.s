.code
daddi r1,r1,1
loop1: daddi r3,r3,234
	daddi r2,r2,5
	dmult r3,r2 
	ddiv  r3,r2	    
	daddi r4,r4,8	
	daddi r5,r5,8 	
	beq R4, R5, loop2	;Taken

	daddi r2,r2,5
	dmult r3,r2 
	ddiv  r3,r2	    

loop2: daddi r2,r3,25
	ddiv r3,r2
	beq r1,r0,END		;Not taken
	daddi r2,r2,5
	dmult r3,r2 
	ddiv  r3,r2	   

loop3: daddi r3,r3,234
	daddi r2,r2,5
	dmult r3,r2 
	ddiv  r3,r2	    
	

	beq R4, R5, loop4	;Taken

	daddi r2,r2,5
	dmult r3,r2 
	ddiv  r3,r2	 

loop4: daddi r2,r3,25
	ddiv r3,r2
	beq r1,r0,END		;Not taken
	
	daddi r2,r2,5
	dmult r3,r2 
	ddiv  r3,r2	 

loop5: daddi r3,r3,234
	daddi r2,r2,5
	dmult r3,r2 
	ddiv  r3,r2	    
	
	beq R4, R5, loop5	;Taken

loop5: daddi r2,r3,25
	ddiv r3,r2
	beq r1,r0,END		;Not taken
	
	daddi r2,r2,5
	dmult r3,r2 
	ddiv  r3,r2	 

	SYSCALL 0
