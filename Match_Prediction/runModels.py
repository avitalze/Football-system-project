import pandas as pd
import numpy as np
import seaborn
from sklearn import metrics
from numpy import sort
from sklearn.model_selection import RandomizedSearchCV
import matplotlib.pyplot as plt
from sklearn.ensemble import RandomForestClassifier, RandomForestRegressor
from sklearn.feature_selection import SelectFromModel
from sklearn.metrics import accuracy_score
from sklearn.model_selection import GridSearchCV, StratifiedKFold, cross_val_score
from sklearn import svm
from sklearn.naive_bayes import GaussianNB
from xgboost import XGBClassifier, plot_importance
import time
from sklearn import linear_model
from sklearn.metrics import confusion_matrix
from sklearn.utils import class_weight
from xgboost import plot_importance

pd.set_option('display.max_columns', None)
df= pd.read_csv("features.csv")
print("start nan insert")

""" fill Nan's values """
# column of 'mean_overallrating_home_team'
df['mean_overallrating_home_team'] = df['mean_overallrating_home_team'].fillna(
    df['mean_overallrating_home_team'].mean())
# column of 'mean_overallrating_away_team'
df['mean_overallrating_away_team'] = df['mean_overallrating_away_team'].fillna(
    df['mean_overallrating_away_team'].mean())
# column of 'mean_gamblingSites_home_win'
df['mean_gamblingSites_home_win'] = df['mean_gamblingSites_home_win'].fillna(
    df['mean_gamblingSites_home_win'].mean())
# column of 'mean_gamblingSites_away_win'
df['mean_gamblingSites_away_win'] = df['mean_gamblingSites_away_win'].fillna(
    df['mean_gamblingSites_away_win'].mean())
# column of 'mean_gamblingSites_draw'
df['mean_gamblingSites_draw'] = df['mean_gamblingSites_draw'].fillna(
    df['mean_gamblingSites_draw'].mean())


#df['result'].replace([0], 'Draw', inplace=True)
#df['result'].replace([1], 'Win', inplace=True)
#df['result'].replace([-1], 'Loose', inplace=True)
#df['result']= df['result'].astype('category')

#df['season'].astype('category')
#df=pd.get_dummies(df, columns=['league_id'])

df.drop(['date','id','league_id','home_team_api_id','away_team_api_id','match_api_id','stage'],axis=1,inplace=True)

test= df[df['season']== '2015/2016']
train= df[df['season']!= '2015/2016']
#test= df[df['season_2015/2016']== 1]
#train= df[df['season_2015/2016']== 0]

"""
X_train= train.drop('result',axis=1)
Y_train= train['result']
X_test= test.drop('result',axis=1)
Y_test= test['result']
"""
train.drop('season',axis=1,inplace=True)
test.drop('season',axis=1,inplace=True)
#df.drop('season',axis=1,inplace=True)

X_train= train.drop(['result'],axis=1)
Y_train= train['result']
X_test= test.drop(['result'],axis=1)
Y_test= test['result']

feature_len= X_train.shape[1]


tic = time.perf_counter()
nb=GaussianNB()
forest= RandomForestClassifier(n_estimators=1000, max_depth=6, max_features=10, min_samples_leaf=5, min_samples_split=8,
                               bootstrap=True,random_state=42,class_weight='balanced')
nb.fit(X_train,Y_train)
y_pred = nb.predict(X_test)
#predictions = [round(value) for value in y_pred]
# evaluate predictions
accuracy = accuracy_score(Y_test, y_pred)
print("GaussianNB Accuracy: %.2f%%" % (accuracy * 100.0))
toc = time.perf_counter()
print("GaussianNB runtime: %.3f seconds" % (toc - tic))







tic = time.perf_counter()
class_weights = class_weight.compute_class_weight('balanced',
                                                 np.unique(Y_train),
                                                 Y_train)
grid = {'max_depth':10,  'n_estimators':1000, 'eta':0.01,'bootstrap':True,'random_state':42, 'gamma':0}

model = XGBClassifier(grid, weights= class_weights)
model.fit(X_train,Y_train)
from sklearn.metrics import accuracy_score
y_pred = model.predict(X_test)
# evaluate predictions
accuracy = accuracy_score(Y_test, y_pred)
print("XGBClassifier Accuracy: %.2f%%" % (accuracy * 100.0))
toc = time.perf_counter()
print("XGBClassifier runtime: %.3f seconds" % (toc - tic))



tic = time.perf_counter()
model = svm.SVC(class_weight='balanced')
model.fit(X_train, Y_train)
y_pred = model.predict(X_test)
# evaluate predictions
accuracy = accuracy_score(Y_test, y_pred)
print("SVM Accuracy: %.2f%%" % (accuracy * 100.0))
toc = time.perf_counter()
print("SVM runtime: %.3f seconds" % (toc - tic))

from sklearn.neighbors import KNeighborsClassifier

tic = time.perf_counter()
knn = KNeighborsClassifier(n_neighbors=60)
knn.fit(X_train, Y_train)
y_pred = knn.predict(X_test)
accuracy = accuracy_score(Y_test, y_pred)
print("KNeighborsClassifier Accuracy: %.2f%%" % (accuracy * 100.0))
toc = time.perf_counter()
print("KNeighborsClassifier runtime: %.3f seconds" % (toc - tic))

