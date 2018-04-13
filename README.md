# Interactive Experiments

### A Quick Intronuctiob with Learning Theories (For academic)

Creating educational apps with theories of learning enhances the impact of using these apps. Building educational apps with the four pillars of learning -active, engaging, meaningful and socially interactive learning- increases effectiveness. 

The four pillars of this learner-centric structure can be summarized as: 

1. Active learning to encourage showing mental effort on practices. 
2. Engaged learning to keep learners concentrated on the current activity. 
3. Meaningful learning to build a deeper understanding by combining different subjects. 
4. Socially interactive learning to support the discussions between learners to analyze different perspectives.

Although using technology eases the implementation of these learning theories to educational apps, Buckingham argues that technology makes only a minor contribution to current educational tools. Lack of technologically enhanced educational applications directed us to create a learning environment which attracts the attention directly to the teaching object, support new inquiry techniques and engage students with minds-on activities. 

We have designed this environment as a smartphone application to access to great variety of learners. 

Our learning environment, “Interactive Experiments”, is created to enhance the engagement of students and support diverse groups in a single application. 

The application includes three inter-related components -Chat Module, Physics Simulations Module, Programming Module- to construct the four pillars of learning: 

1. Minds on learning means creating different perspectives by showing mental effort on activities. We aimed to increase this effort with setting the physics simulation parameters with using code blocks and sensors (e.g. tilting phone to set the angle of inclined plane). 
2. Engaged learning builds a continuous interaction through the interface to increase the attention and the amount of learned material. We put navigation option and teaching assistance to Chat Module to keep the learner’s attention on to subject and build a continuous flow. 
3. Meaningful learning promotes creating and testing hypothesis. We served an easy-to-use environment to manipulate experimental variables to test hypothesis and grasp the conceptual model behind the experiments. 
4. Socially interactive environment supports discussions in classroom to understand the newly encountered material. We enabled the classroom-chat in the Chat Module to keep the classroom connected.

### A Quick Introduction to Software (For developers)

All key points you need to know:

- CodeNotesInteractive folder is the Android Project name. 
- Physics Simulations are JS Simulations by Walter Fendt. They are updated visually and modified to communicate with Android System.
- If you are going to take a quick look, first check .util.FragmentManager then HomeActivity to understand the structure of the project.
- Chat bot is implemented with DialogFlow and Firebase.
- Programming blocks are Google's Blocky Project.
- Every fragment is inflated on HomeActivity, other activities are stand alone activities.
- Fragment logic is guided with util.FragmentManager.
- Logic and view of Study Notes are mostly taken from: https://github.com/avjinder/Minimal-Todo
- Navigation Drawer implementation: https://github.com/mikepenz/MaterialDrawer
- Tutorial View: https://github.com/msayan/tutorial-view
- Blockly requires minSdk->18, ai.api requires minSdk->16






