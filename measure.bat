cd src
for %%N IN (5 10 15 20 25 30 35 40 45 50 55 60 65 70 75 80 85 90 95 100) DO java ComputeTardiness ..\test-set\instances\random_RDD=0.2_TF=0.2_#%%N.dat tests
