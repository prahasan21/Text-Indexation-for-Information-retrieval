
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import java.nio	.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;



  
  
 public class generateIndex { 
	 public static void main(String[] args) throws IOException { 
		 
		 
		 
		 	String corpusFile = "D:\\Search_JAVA\\corpus\\";
		 	String indexFile = "D:\\Search_JAVA\\index_file\\";
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			Directory dir = FSDirectory.open(Paths.get(indexFile));
			IndexWriter writer = new IndexWriter(dir, iwc);
			File[] all_files = new File(corpusFile).listFiles();
		 for(File file : all_files)
		 {
			 if (file.isFile())
			 {
			 
		 File f = new File(corpusFile+file.getName()+"");
		 System.out.println("Converting File"+file.getName()+" to string....");
		 String each_file = FileUtils.readFileToString(f);
		 //System.out.println(each_file);
		 
		  //File file = new File("D:\\Search_JAVA\\corpus\\AP890101.trectext");
		  
		  String[] docs = StringUtils.substringsBetween(each_file, "<DOC>", "</DOC>");
		  
			//for (String doc : docs) 
				for(int i=0;i<docs.length;i++)
			{
				
					Document luceneDoc = new Document();
					String docNo = "";
					String DOCNO[] = StringUtils.substringsBetween(docs[i], "<DOCNO>", "</DOCNO>");
					//String docno = DOCNO.toString();
					for(int d=0;d<DOCNO.length;d++) 
					{
						docNo += " "+DOCNO[d];
					}
					System.out.println("......Inner Document DOCNO:"+docNo+"being added........");
					luceneDoc.add(new StringField("DOCNO",docNo,Field.Store.YES));
					
					
					if(StringUtils.substringBetween(docs[i], "<HEAD>", "</HEAD>")!=null)
					{	
						String head = "";
						String HEAD[] = StringUtils.substringsBetween(docs[i], "<HEAD>", "</HEAD>");
						for(int h=0;h<HEAD.length;h++) 
						{
							head += " "+HEAD[h];
						}
						//String head = HEAD.toString();
						luceneDoc.add(new TextField("HEAD", head, Field.Store.YES));
					}
					

					
					if(StringUtils.substringBetween(docs[i], "<BYLINE>", "</BYLINE>")!=null)
					{
						String byLine = "";
						String BYLINE[] = StringUtils.substringsBetween(docs[i], "<BYLINE>", "</BYLINE>");
						for(int b=0;b<BYLINE.length;b++) 
						{
							byLine += " "+BYLINE[b];
						}
						//String byline = BYLINE.toString();
						luceneDoc.add(new TextField("BYLINE", byLine, Field.Store.YES));
					}
					

				
					if(StringUtils.substringsBetween(docs[i], "<DATELINE>", "</DATELINE>")!=null)
					{
						String dateLine = "";
						String DATELINE[] = StringUtils.substringsBetween(docs[i], "<DATELINE>", "</DATELINE>");
						for(int dt=0;dt<DATELINE.length;dt++) 
						{
							dateLine += " "+DATELINE[dt];
						}
						luceneDoc.add(new TextField("DATELINE", dateLine, Field.Store.YES));
					}
					

					if(StringUtils.substringsBetween(docs[i], "<TEXT>", "</TEXT>")!=null)
					{
						String text = "";
						String TEXT[] = StringUtils.substringsBetween(docs[i], "<TEXT>", "</TEXT>");
						for(int t=0;t<TEXT.length;t++) 
						{
							text += " "+TEXT[t];
						}
						luceneDoc.add(new TextField("TEXT", text, Field.Store.YES));
					}
					
					
					writer.addDocument(luceneDoc);
					
				
			}
			
	 }
		 
 }
	 	 
		 writer.close();
		 IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
		 Terms vocabulary = MultiFields.getTerms(reader, "TEXT");
			TermsEnum iterator = vocabulary.iterator();
			BytesRef byteRef = null;
			int c = 0;
			//Vocabulary is printed here but not in indexComparison.java file"
			System.out.println("\n*******Vocabulary-Start**********");
			while ((byteRef = iterator.next()) != null) {
				String term = byteRef.utf8ToString();
				System.out.println(term);
				c += 1;
			}
			System.out.println("\n*******Vocabulary-End**********");
			
			System.out.println("Total number of documents in the corpus:"+ reader.maxDoc());
			 System.out
				.println("Number of documents containing the term \"new\" for field \"TEXT\": "
						+ reader.docFreq(new Term("TEXT", "new")));
				System.out
				.println("Number of occurrences of \"new\" in the field\"TEXT\": "
						+ reader.totalTermFreq(new Term("TEXT", "new")));
				
						
				System.out.println("Size of the vocabulary for this field: "
						+ c);

				System.out
						.println("Number of documents that have at least one term for this field: "
								+ vocabulary.getDocCount());

				System.out.println("Number of tokens for this field: "
						+ vocabulary.getSumTotalTermFreq());

				System.out.println("Number of postings for this field: "
						+ vocabulary.getSumDocFreq());
				reader.close();
			
 }
	 }
 