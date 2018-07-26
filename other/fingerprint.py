
class RFP:
	def __init__(self,min_size,avg_size,max_size,d):
		self.min_size=min_size
		self.q=avg_size
		self.d=d
		self.dd=d**(min_size-1)
		self.max_size=max_size
		self.mask=self.q-1
		self.cur_rabin=0
		self.cur_size=0
		self.cur_loc=0
		self.cur_first=0
		self.data=b""
		self.chunks=[]
		self.length=0
		self.last_loc=0
		
	def load_data(self,path):
		f=open(path,'r')
		self.data=f.read()
		self.length=len(self.data)
		f.close()
		
	def rabin(self):
		if self.cur_loc==0:
			for i in range(self.min_size-1,-1,-1):
				self.cur_rabin+=self.d*(ord(self.data[i])+self.cur_rabin)
			self.cur_rabin=self.cur_rabin % self.q
		else:
			self.cur_rabin=(self.d*(self.cur_rabin-self.dd*self.cur_first)+ord(self.data[self.cur_loc+self.min_size]))%self.q
		self.cur_first=ord(self.data[self.cur_loc])
		return self.cur_rabin
		
	def chunking(self):
		while self.cur_loc+self.min_size+1<self.length:
			while self.cur_size<self.max_size and self.cur_loc+self.min_size+1<self.length:
				if self.rabin()==self.mask:
					self.chunks.append(self.data[self.last_loc:(self.cur_loc+self.min_size)])
					self.last_loc=self.cur_loc+self.min_size
					self.cur_loc=self.last_loc
					self.cur_size=0
				self.cur_size+=1
				self.cur_loc+=1
			if self.cur_size==self.max_size:
				self.chunks.append(self.data[self.last_loc:(self.last_loc+self.max_size)])
				self.cur_size=0
				for i in range(0,self.min_size):
					if self.cur_loc+self.min_size+1==self.length:
						break
					self.rabin()
					self.cur_loc+=1
				self.last_loc=self.cur_loc
			if self.cur_loc+self.min_size+1==self.length:
				self.chunks.append(self.data[self.last_loc:])
	

a=RFP(2,4,8,257)
a.load_data('/users/xingyue/Dropbox/2015_sem1/csci4180/hw3/test')
a.chunking()
