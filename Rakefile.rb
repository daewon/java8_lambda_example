file "Lambda.java"

task :clean do
  system "rm *.class *~"
end

task compile: [:clean] do
  system "javac Lambda.java"
end

task run: [:compile] do
  system "java Lambda"
end

task default: [:run] do
end
