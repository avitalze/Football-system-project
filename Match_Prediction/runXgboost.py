import pandas as pd
import numpy as np
import seaborn
from xgboost import XGBClassifier, plot_importance
import time
from sklearn.utils import class_weight
from xgboost import plot_importance
import matplotlib.pyplot as plt
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





tic = time.perf_counter()
class_weights = class_weight.compute_class_weight('balanced',
                                                 np.unique(Y_train),
                                                 Y_train)
grid = {'max_depth':10,  'n_estimators':1000, 'eta':0.01,'bootstrap':True,'random_state':42, 'gamma':0}

model = XGBClassifier(grid, weights= class_weights)
model.fit(X_train,Y_train)
from sklearn.metrics import accuracy_score, confusion_matrix

y_pred = model.predict(X_test)
# evaluate predictions
accuracy = accuracy_score(Y_test, y_pred)
print("XGBClassifier Accuracy: %.2f%%" % (accuracy * 100.0))
toc = time.perf_counter()
print("XGBClassifier runtime: %.3f seconds" % (toc - tic))

cm = confusion_matrix(Y_test, y_pred)
ax= plt.subplot()
seaborn.heatmap(cm, fmt='',annot=True, ax = ax,cmap="YlGnBu"); #annot=True to annotate cells
# labels, title and ticks
ax.set_xlabel('Predicted labels')
ax.set_ylabel('True labels')
ax.set_title('Confusion Matrix')
#ax.xaxis.set_ticklabels(['not diabetic', 'is diabetic'])
#ax.yaxis.set_ticklabels(['not diabetic', 'is diabetic'])

plt.show()


import shap

# load JS visualization code to notebook
shap.initjs()


shap_values=shap.TreeExplainer(model).shap_values(X_test)
print(shap_values)
shap.summary_plot(shap_values[0], X_test)

plot_importance(model)
plt.show()